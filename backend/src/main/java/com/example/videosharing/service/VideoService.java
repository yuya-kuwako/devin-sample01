package com.example.videosharing.service;

import com.example.videosharing.dto.VideoDto;
import com.example.videosharing.model.User;
import com.example.videosharing.model.Video;
import com.example.videosharing.repository.UserRepository;
import com.example.videosharing.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<VideoDto> getLatestVideos() {
        return videoRepository.findLatestVideos().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<VideoDto> getTrendingVideos() {
        return videoRepository.findTrendingVideos().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<VideoDto> searchVideos(String query) {
        return videoRepository.searchVideos(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public VideoDto getVideoById(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
        return convertToDto(video);
    }

    public List<VideoDto> getVideosByUserId(Long userId) {
        return videoRepository.findByUserIdOrderByUploadedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public VideoDto createVideo(Video video, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        video.setUser(user);
        return convertToDto(videoRepository.save(video));
    }

    public VideoDto updateVideo(Long id, Video videoDetails) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
        
        video.setTitle(videoDetails.getTitle());
        video.setDescription(videoDetails.getDescription());
        video.setThumbnailUrl(videoDetails.getThumbnailUrl());
        video.setTags(videoDetails.getTags());
        
        return convertToDto(videoRepository.save(video));
    }

    public void deleteVideo(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
        videoRepository.delete(video);
    }

    public VideoDto incrementViews(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
        video.setViews(video.getViews() + 1);
        return convertToDto(videoRepository.save(video));
    }

    public VideoDto likeVideo(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
        video.setLikes(video.getLikes() + 1);
        return convertToDto(videoRepository.save(video));
    }

    public VideoDto dislikeVideo(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
        video.setDislikes(video.getDislikes() + 1);
        return convertToDto(videoRepository.save(video));
    }

    private VideoDto convertToDto(Video video) {
        VideoDto videoDto = new VideoDto();
        videoDto.setId(video.getId());
        videoDto.setTitle(video.getTitle());
        videoDto.setDescription(video.getDescription());
        videoDto.setVideoUrl(video.getVideoUrl());
        videoDto.setThumbnailUrl(video.getThumbnailUrl());
        videoDto.setViews(video.getViews());
        videoDto.setLikes(video.getLikes());
        videoDto.setDislikes(video.getDislikes());
        videoDto.setUploadedAt(video.getUploadedAt());
        videoDto.setTags(video.getTags());
        videoDto.setCommentCount(video.getComments().size());
        
        if (video.getUser() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(video.getUser().getId());
            userDto.setUsername(video.getUser().getUsername());
            userDto.setProfilePictureUrl(video.getUser().getProfilePictureUrl());
            videoDto.setUser(userDto);
        }
        
        return videoDto;
    }
}
