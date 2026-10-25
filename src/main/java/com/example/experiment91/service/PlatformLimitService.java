package com.example.experiment91.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PlatformLimitService {

    private static final Map<String, Integer> LIMITS = Map.of(
            "TWITTER", 280,
            "LINKEDIN", 3000,
            "INSTAGRAM", 2200);

    public int getLimit(String platform) {

        if (platform == null) {
            throw new IllegalArgumentException("Platform is required");
        }

        Integer limit = LIMITS.get(platform.toUpperCase());

        if (limit == null) {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }

        return limit;
    }

    public boolean isWithinLimit(String platform, String content) {
        return content != null && content.length() <= getLimit(platform);
    }
}