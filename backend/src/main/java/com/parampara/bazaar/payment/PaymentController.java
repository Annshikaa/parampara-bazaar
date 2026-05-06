package com.parampara.bazaar.payment;

import com.parampara.bazaar.common.dto.ApiResponse;
import com.parampara.bazaar.common.dto.CreatePaymentRequest;
import com.parampara.bazaar.common.dto.CreatePaymentResponse;
import com.parampara.bazaar.common.dto.VerifyPaymentRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Buyer creates razorpay order for an internal orderId
    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/create")
    public CreatePaymentResponse create(@Valid @RequestBody CreatePaymentRequest req) {
        return paymentService.createRazorpayOrder(req.getOrderId());
    }

    // Called after Razorpay checkout success on frontend
    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/verify")
    public ApiResponse verify(@Valid @RequestBody VerifyPaymentRequest req) {
        paymentService.verifyAndMarkPaid(req);
        return new ApiResponse(true, "Payment verified. Order marked PAID.");
    }
}
