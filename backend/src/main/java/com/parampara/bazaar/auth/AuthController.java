package com.parampara.bazaar.auth;

import com.parampara.bazaar.common.dto.ApiResponse;
import com.parampara.bazaar.common.dto.LoginRequest;
import com.parampara.bazaar.common.dto.LoginResponse;
import com.parampara.bazaar.common.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new ApiResponse(true, "Registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new LoginResponse(true, "Login successful", token));
    }
}
