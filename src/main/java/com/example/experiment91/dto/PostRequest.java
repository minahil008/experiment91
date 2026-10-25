package com.example.experiment91.dto;

import jakarta.validation.constraints.NotBlank;

public record PostRequest(
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Content is required") String content,
        @NotBlank(message = "Platform is required") String platform) {
}