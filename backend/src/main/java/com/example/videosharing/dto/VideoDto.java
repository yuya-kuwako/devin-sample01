package com.example.videosharing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Long views;
    private Long likes;
    private Long dislikes;
    private LocalDateTime uploadedAt;
    private UserDto user;
    private List<String> tags;
    private int commentCount;
}
