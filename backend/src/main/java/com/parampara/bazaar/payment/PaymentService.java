package com.parampara.bazaar.payment;

import com.parampara.bazaar.common.SecurityUtil;
import com.parampara.bazaar.common.dto.CreatePaymentResponse;
import com.parampara.bazaar.common.dto.VerifyPaymentRequest;
import com.parampara.bazaar.common.enums.OrderStatus;
import com.parampara.bazaar.order.Order;
import com.parampara.bazaar.order.OrderRepository;
import com.parampara.bazaar.user.User;
import com.parampara.bazaar.user.UserRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final String keyId;
    private final String keySecret;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          UserRepository userRepository,
                          @Value("${app.razorpay.keyId}") String keyId,
                          @Value("${app.razorpay.keySecret}") String keySecret) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.keyId = keyId;
        this.keySecret = keySecret;
    }

    private User currentUser() {
        String email = SecurityUtil.currentEmail();
        if (email == null) throw new RuntimeException("Unauthorized");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public CreatePaymentResponse createRazorpayOrder(Long internalOrderId) {
        User user = currentUser();

        Order order = orderRepository.findById(internalOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        if (order.getStatus() == OrderStatus.PAID) {
            throw new RuntimeException("Order already paid");
        }

        // Razorpay amount in paise
        long amountPaise = Math.round(order.getTotalMinPrice() * 100);

        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);

            JSONObject options = new JSONObject();
            options.put("amount", amountPaise);
            options.put("currency", "INR");
            options.put("receipt", "order_rcpt_" + internalOrderId);

            com.razorpay.Order rpOrder = client.orders.create(options);

            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setRazorpayOrderId(rpOrder.get("id"));
            payment.setAmount(order.getTotalMinPrice());
            payment.setCurrency("INR");
            payment.setStatus("CREATED");

            paymentRepository.save(payment);

            return new CreatePaymentResponse(
                    rpOrder.get("id"),
                    keyId,
                    order.getTotalMinPrice(),
                    "INR"
            );

        } catch (RazorpayException e) {
            throw new RuntimeException("Razorpay error: " + e.getMessage());
        }
    }

    @Transactional
    public void verifyAndMarkPaid(VerifyPaymentRequest req) {

        Payment payment = paymentRepository.findByRazorpayOrderId(req.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Payment record not found"));

        // Verify signature: HMAC_SHA256(order_id|payment_id, keySecret)
        String data = req.getRazorpayOrderId() + "|" + req.getRazorpayPaymentId();
        String expected = hmacSha256(data, keySecret);

        if (!expected.equals(req.getRazorpaySignature())) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new RuntimeException("Invalid payment signature");
        }

        payment.setRazorpayPaymentId(req.getRazorpayPaymentId());
        payment.setRazorpaySignature(req.getRazorpaySignature());
        payment.setStatus("PAID");
        paymentRepository.save(payment);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("HMAC error: " + e.getMessage());
        }
    }
}
