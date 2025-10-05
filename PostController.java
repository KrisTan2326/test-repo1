package com.socio.controller;

import com.socio.model.Post;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "3. Posts & Interactions", description = "APIs for creating and interacting with posts")
public class PostController {

    private static final Map<Long, Post> postStore = new ConcurrentHashMap<>();
    private static final AtomicLong postIdCounter = new AtomicLong();

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post newPost) {
        newPost.setId(postIdCounter.incrementAndGet());
        postStore.put(newPost.getId(), newPost);
        return ResponseEntity.ok(newPost);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postStore.values().stream().toList());
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        Post post = postStore.get(postId);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        post.getLikes().incrementAndGet();
        return ResponseEntity.ok("Post liked! Total likes: " + post.getLikes().get());
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long postId, @RequestBody String comment) {
        Post post = postStore.get(postId);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        post.getComments().add(comment);
        return ResponseEntity.ok("Comment added.");
    }
}