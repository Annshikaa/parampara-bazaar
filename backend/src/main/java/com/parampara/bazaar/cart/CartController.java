package com.parampara.bazaar.cart;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parampara.bazaar.common.dto.AddToCartRequest;
import com.parampara.bazaar.common.dto.ApiResponse;
import com.parampara.bazaar.common.dto.UpdateCartQtyRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/add")
    public CartItem add(@Valid @RequestBody AddToCartRequest req) {
        return cartService.add(req);
    }

    @PreAuthorize("hasRole('BUYER')")
    @PutMapping("/qty")
    public CartItem updateQty(@Valid @RequestBody UpdateCartQtyRequest req) {
        return cartService.updateQty(req);
    }

    @PreAuthorize("hasRole('BUYER')")
    @DeleteMapping("/remove/{productId}")
    public ApiResponse remove(@PathVariable Long productId) {
        cartService.remove(productId);
        return new ApiResponse(true, "Removed from cart");
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping
    public CartResponse myCart() {
        return cartService.myCart();
    }

    @PreAuthorize("hasRole('BUYER')")
    @DeleteMapping("/clear")
    public ApiResponse clear() {
        cartService.clear();
        return new ApiResponse(true, "Cart cleared");
    }
}
