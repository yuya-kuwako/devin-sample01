package com.example.videosharing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    
    @NotBlank(message = "Comment content is required")
    private String content;
    
    private LocalDateTime createdAt;
    private Long likes;
    private Long dislikes;
    private UserDto user;
    private Long videoId;
}
