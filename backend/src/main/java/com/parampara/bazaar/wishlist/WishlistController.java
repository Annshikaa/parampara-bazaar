package com.parampara.bazaar.wishlist;

import com.parampara.bazaar.common.dto.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // Buyer adds product to wishlist
    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/{productId}")
    public WishlistItem add(@PathVariable Long productId) {
        return wishlistService.add(productId);
    }

    // Buyer removes product from wishlist
    @PreAuthorize("hasRole('BUYER')")
    @DeleteMapping("/{productId}")
    public ApiResponse remove(@PathVariable Long productId) {
        wishlistService.remove(productId);
        return new ApiResponse(true, "Removed from wishlist");
    }

    // Buyer views wishlist
    @PreAuthorize("hasRole('BUYER')")
    @GetMapping
    public List<WishlistItem> myWishlist() {
        return wishlistService.myWishlist();
    }

    // Buyer checks if product is wishlisted
    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/check/{productId}")
    public ApiResponse check(@PathVariable Long productId) {
        boolean yes = wishlistService.isWishlisted(productId);
        return new ApiResponse(true, yes ? "YES" : "NO");
    }
}
