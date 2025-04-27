package com.example.videosharing.controller;

import com.example.videosharing.dto.VideoDto;
import com.example.videosharing.model.Video;
import com.example.videosharing.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public ResponseEntity<List<VideoDto>> getAllVideos() {
        return ResponseEntity.ok(videoService.getAllVideos());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<VideoDto>> getLatestVideos() {
        return ResponseEntity.ok(videoService.getLatestVideos());
    }

    @GetMapping("/trending")
    public ResponseEntity<List<VideoDto>> getTrendingVideos() {
        return ResponseEntity.ok(videoService.getTrendingVideos());
    }

    @GetMapping("/search")
    public ResponseEntity<List<VideoDto>> searchVideos(@RequestParam String query) {
        return ResponseEntity.ok(videoService.searchVideos(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDto> getVideoById(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.getVideoById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VideoDto>> getVideosByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(videoService.getVideosByUserId(userId));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<VideoDto> createVideo(@Valid @RequestBody Video video, @PathVariable Long userId) {
        return new ResponseEntity<>(videoService.createVideo(video, userId), HttpStatus.CREATED);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "uploads/videos";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            
            Files.copy(file.getInputStream(), filePath);
            
            return ResponseEntity.ok("/api/videos/stream/" + filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload video: " + e.getMessage());
        }
    }

    @PostMapping("/thumbnail/upload")
    public ResponseEntity<String> uploadThumbnail(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "uploads/thumbnails";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            
            Files.copy(file.getInputStream(), filePath);
            
            return ResponseEntity.ok("/api/videos/thumbnail/" + filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload thumbnail: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoDto> updateVideo(@PathVariable Long id, @Valid @RequestBody Video videoDetails) {
        return ResponseEntity.ok(videoService.updateVideo(id, videoDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<VideoDto> incrementViews(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.incrementViews(id));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<VideoDto> likeVideo(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.likeVideo(id));
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<VideoDto> dislikeVideo(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.dislikeVideo(id));
    }
}
