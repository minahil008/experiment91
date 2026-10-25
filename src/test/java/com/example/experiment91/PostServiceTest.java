package com.example.experiment91;

import com.example.experiment91.dto.PostRequest;
import com.example.experiment91.model.Post;
import com.example.experiment91.repository.PostRepository;
import com.example.experiment91.service.PlatformLimitService;
import com.example.experiment91.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    private PostService postService;

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, new PlatformLimitService());
    }

    @Test
    void createPostSavesWhenContentIsWithinLimit() {
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        Post saved = postService.createPost(new PostRequest("Hello", "A short post", "Twitter"));

        assertEquals("TWITTER", saved.getPlatform());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void createPostRejectsContentOverLimitAndNeverTouchesTheDatabase() {
        PostRequest tooLong = new PostRequest("Hello", "a".repeat(281), "Twitter");

        assertThrows(IllegalArgumentException.class, () -> postService.createPost(tooLong));

        verify(postRepository, never()).save(any(Post.class));
    }
}