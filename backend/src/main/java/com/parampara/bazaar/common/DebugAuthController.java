package com.parampara.bazaar.common;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugAuthController {

    @GetMapping("/debug/auth")
    public String debug(Authentication auth) {
        return "email=" + auth.getName() + " authorities=" + auth.getAuthorities();
    }
}

