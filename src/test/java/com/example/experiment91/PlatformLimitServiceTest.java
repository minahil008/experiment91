package com.example.experiment91;

import com.example.experiment91.service.PlatformLimitService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlatformLimitServiceTest {

    private final PlatformLimitService service = new PlatformLimitService();

    @Test
    void knownPlatformsHaveCorrectLimits() {
        assertAll(
                () -> assertEquals(280, service.getLimit("Twitter")),
                () -> assertEquals(3000, service.getLimit("LinkedIn")),
                () -> assertEquals(2200, service.getLimit("Instagram")));
    }

    @Test
    void platformNameIsCaseInsensitive() {
        assertEquals(280, service.getLimit("twitter"));
        assertEquals(280, service.getLimit("TWITTER"));
    }

    @Test
    void postOfExactly280CharactersIsAllowedOnTwitter() {
        assertTrue(service.isWithinLimit("Twitter", "a".repeat(280)));
    }

    @Test
    void postOf281CharactersExceedsTwitterLimit() {
        assertFalse(service.isWithinLimit("Twitter", "a".repeat(281)));
    }

    @Test
    void unknownPlatformThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.getLimit("Snapchat"));
    }
}