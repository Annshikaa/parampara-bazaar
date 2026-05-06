package com.parampara.bazaar.vendor;

import com.parampara.bazaar.common.SecurityUtil;
import com.parampara.bazaar.user.User;
import com.parampara.bazaar.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    public VendorService(VendorRepository vendorRepository, UserRepository userRepository) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
    }

    public VendorProfile createOrUpdateProfile(VendorProfileRequest req) {
        String email = SecurityUtil.currentEmail();
        if (email == null) throw new RuntimeException("Unauthorized");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VendorProfile profile = vendorRepository.findByUser(user).orElse(new VendorProfile());
        profile.setUser(user);
        profile.setShopName(req.getShopName());
        profile.setLocation(req.getLocation());
        profile.setVendorType(req.getVendorType());

        return vendorRepository.save(profile);
    }

    public VendorProfile myProfile() {
        String email = SecurityUtil.currentEmail();
        if (email == null) throw new RuntimeException("Unauthorized");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return vendorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Vendor profile not created yet"));
    }
}
