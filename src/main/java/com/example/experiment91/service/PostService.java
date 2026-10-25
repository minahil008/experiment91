package com.example.experiment91.service;

import com.example.experiment91.dto.PostRequest;
import com.example.experiment91.model.Post;
import com.example.experiment91.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PlatformLimitService platformLimitService;

    public PostService(PostRepository postRepository, PlatformLimitService platformLimitService) {
        this.postRepository = postRepository;
        this.platformLimitService = platformLimitService;
    }

    public Post createPost(PostRequest request) {

        if (!platformLimitService.isWithinLimit(request.platform(), request.content())) {
            throw new IllegalArgumentException(
                    "Content exceeds the " + request.platform() + " limit of "
                            + platformLimitService.getLimit(request.platform()) + " characters");
        }

        Post post = new Post(request.title(), request.content(), request.platform().toUpperCase());

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + id));
    }
}