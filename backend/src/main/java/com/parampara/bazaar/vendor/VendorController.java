package com.parampara.bazaar.vendor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PreAuthorize("hasRole('VENDOR')")
    @PostMapping("/profile")
    public String createOrUpdate(@Valid @RequestBody VendorProfileRequest req) {
        VendorProfile vp = vendorService.createOrUpdateProfile(req);
        return "OK profile saved. id=" + vp.getId();
    }

    @PreAuthorize("hasRole('VENDOR')")
    @GetMapping("/profile")
    public String myProfile() {
        VendorProfile vp = vendorService.myProfile();
        return "OK profile found. id=" + vp.getId();
    }
}

