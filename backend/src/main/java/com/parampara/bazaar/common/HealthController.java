package com.parampara.bazaar.common;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Parampara Bazaar Backend is running 🚀";
    }

    @GetMapping("/hash")
    public String hash(@RequestParam String p, PasswordEncoder encoder) {
        return encoder.encode(p);
    }

}
