package com.parampara.bazaar.vendor;

import com.parampara.bazaar.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<VendorProfile, Long> {
    Optional<VendorProfile> findByUser(User user);
}
