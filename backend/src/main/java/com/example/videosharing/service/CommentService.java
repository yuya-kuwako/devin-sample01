package com.example.videosharing.service;

import com.example.videosharing.dto.CommentDto;
import com.example.videosharing.dto.UserDto;
import com.example.videosharing.model.Comment;
import com.example.videosharing.model.User;
import com.example.videosharing.model.Video;
import com.example.videosharing.repository.CommentRepository;
import com.example.videosharing.repository.UserRepository;
import com.example.videosharing.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    public List<CommentDto> getCommentsByVideoId(Long videoId) {
        return commentRepository.findByVideoIdOrderByCreatedAtDesc(videoId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        return convertToDto(comment);
    }

    public CommentDto createComment(Comment comment, Long userId, Long videoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));
        
        comment.setUser(user);
        comment.setVideo(video);
        
        return convertToDto(commentRepository.save(comment));
    }

    public CommentDto updateComment(Long id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        
        comment.setContent(commentDetails.getContent());
        
        return convertToDto(commentRepository.save(comment));
    }

    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        commentRepository.delete(comment);
    }

    public CommentDto likeComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        comment.setLikes(comment.getLikes() + 1);
        return convertToDto(commentRepository.save(comment));
    }

    public CommentDto dislikeComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        comment.setDislikes(comment.getDislikes() + 1);
        return convertToDto(commentRepository.save(comment));
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setLikes(comment.getLikes());
        commentDto.setDislikes(comment.getDislikes());
        commentDto.setVideoId(comment.getVideo().getId());
        
        if (comment.getUser() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(comment.getUser().getId());
            userDto.setUsername(comment.getUser().getUsername());
            userDto.setProfilePictureUrl(comment.getUser().getProfilePictureUrl());
            commentDto.setUser(userDto);
        }
        
        return commentDto;
    }
}
