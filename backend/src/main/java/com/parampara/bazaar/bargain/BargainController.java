package com.parampara.bazaar.bargain;

import com.parampara.bazaar.bargain.dto.AcceptRequest;
import com.parampara.bazaar.bargain.dto.BargainResponse;
import com.parampara.bazaar.bargain.dto.ChatRequest;
import com.parampara.bazaar.bargain.dto.StartBargainRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bargain")
public class BargainController {

    private final BargainService bargainService;

    public BargainController(BargainService bargainService) {
        this.bargainService = bargainService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/start")
    public BargainResponse start(@Valid @RequestBody StartBargainRequest req) {
        return bargainService.startSession(req.getProductId());
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/{sessionId}/chat")
    public BargainResponse chat(@PathVariable Long sessionId, @Valid @RequestBody ChatRequest req) {
        return bargainService.chat(sessionId, req);
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/accept")
    public BargainResponse accept(@Valid @RequestBody AcceptRequest req) {
        return bargainService.accept(req.getSessionId());
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/{sessionId}")
    public BargainResponse get(@PathVariable Long sessionId) {
        return bargainService.getSession(sessionId);
    }
}

