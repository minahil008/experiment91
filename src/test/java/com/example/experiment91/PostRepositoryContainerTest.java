package com.example.experiment91;

import com.example.experiment91.model.Post;
import com.example.experiment91.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// disabledWithoutDocker = true  ->  skipped (not failed) on a machine without Docker
@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class PostRepositoryContainerTest {

    // Docker starts a REAL PostgreSQL 15 database just for this test
    @Container
    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:15");

    // Point Spring at the container instead of the H2 database
    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", db::getJdbcUrl);
        registry.add("spring.datasource.username", db::getUsername);
        registry.add("spring.datasource.password", db::getPassword);
    }

    @Autowired
    private PostRepository postRepository;

    @Test
    void savesAndReadsAPostFromARealPostgresDatabase() {
        Post saved = postRepository.save(new Post("Container test", "Stored in real Postgres", "TWITTER"));

        assertNotNull(saved.getId());
        assertTrue(db.isRunning());

        Post found = postRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Stored in real Postgres", found.getContent());
    }
}