package com.example.videosharing.repository;

import com.example.videosharing.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUserIdOrderByUploadedAtDesc(Long userId);
    
    @Query("SELECT v FROM Video v ORDER BY v.uploadedAt DESC")
    List<Video> findLatestVideos();
    
    @Query("SELECT v FROM Video v ORDER BY v.views DESC")
    List<Video> findTrendingVideos();
    
    @Query("SELECT v FROM Video v WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(v.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Video> searchVideos(String query);
}
