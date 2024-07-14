package com.example.mytv.adapter.in.api.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
    Integer statusCode,
    String message,
    LocalDateTime timestamp
) {}
