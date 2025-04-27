package com.example.videosharing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @Column(length = 2000)
    private String description;

    private String videoUrl;

    private String thumbnailUrl;

    private Long views = 0L;

    private Long likes = 0L;

    private Long dislikes = 0L;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection
    private List<String> tags = new ArrayList<>();
}
