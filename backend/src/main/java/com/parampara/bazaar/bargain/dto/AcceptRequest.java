package com.parampara.bazaar.bargain.dto;

import jakarta.validation.constraints.NotNull;

public class AcceptRequest {

    @NotNull
    private Long sessionId;

    public AcceptRequest() {}

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
}
