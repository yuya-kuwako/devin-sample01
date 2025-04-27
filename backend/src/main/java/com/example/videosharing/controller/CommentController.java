package com.example.videosharing.controller;

import com.example.videosharing.dto.CommentDto;
import com.example.videosharing.model.Comment;
import com.example.videosharing.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<CommentDto>> getCommentsByVideoId(@PathVariable Long videoId) {
        return ResponseEntity.ok(commentService.getCommentsByVideoId(videoId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDto>> getCommentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PostMapping("/video/{videoId}/user/{userId}")
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody Comment comment,
            @PathVariable Long userId,
            @PathVariable Long videoId) {
        return new ResponseEntity<>(commentService.createComment(comment, userId, videoId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @Valid @RequestBody Comment commentDetails) {
        return ResponseEntity.ok(commentService.updateComment(id, commentDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<CommentDto> likeComment(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.likeComment(id));
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<CommentDto> dislikeComment(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.dislikeComment(id));
    }
}
