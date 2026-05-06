package com.parampara.bazaar.vendor;

import com.parampara.bazaar.common.SecurityUtil;
import com.parampara.bazaar.common.dto.ApiResponse;
import com.parampara.bazaar.common.enums.OrderItemStatus;
import com.parampara.bazaar.order.OrderItem;
import com.parampara.bazaar.order.OrderItemRepository;
import com.parampara.bazaar.user.User;
import com.parampara.bazaar.user.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendor/orders")
@PreAuthorize("hasRole('VENDOR')")
public class VendorOrdersController {

    private final OrderItemRepository orderItemRepo;
    private final UserRepository userRepo;

    public VendorOrdersController(OrderItemRepository orderItemRepo, UserRepository userRepo) {
        this.orderItemRepo = orderItemRepo;
        this.userRepo = userRepo;
    }

    private User currentVendor() {
        String email = SecurityUtil.currentEmail();
        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    @GetMapping
    public List<OrderItem> myOrderItems() {
        User vendor = currentVendor();
        return orderItemRepo.findByVendorId(vendor.getId());
    }

    @PutMapping("/item/{orderItemId}/status")
    public ApiResponse updateItemStatus(@PathVariable Long orderItemId, @RequestParam OrderItemStatus status) {

        User vendor = currentVendor();

        OrderItem item = orderItemRepo.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        if (!vendor.getId().equals(item.getVendorId()))
            throw new RuntimeException("Forbidden");

        item.setItemStatus(status);
        orderItemRepo.save(item);

        return new ApiResponse(true, "Updated to " + status);
    }
}
