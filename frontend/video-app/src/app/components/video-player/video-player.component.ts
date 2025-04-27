import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';
import { VideoService } from '../../services/video.service';
import { CommentService } from '../../services/comment.service';
import { Video } from '../../models/video';
import { Comment } from '../../models/comment';
import { HeaderComponent } from '../header/header.component';
import { CommentSectionComponent } from '../comment-section/comment-section.component';

@Component({
  selector: 'app-video-player',
  standalone: true,
  imports: [
    CommonModule, 
    RouterModule, 
    FormsModule, 
    MaterialModule, 
    HeaderComponent, 
    CommentSectionComponent
  ],
  templateUrl: './video-player.component.html',
  styleUrl: './video-player.component.scss'
})
export class VideoPlayerComponent implements OnInit {
  video: Video | null = null;
  comments: Comment[] = [];
  relatedVideos: Video[] = [];
  isLoading: boolean = true;
  newComment: string = '';
  
  constructor(
    private route: ActivatedRoute,
    private videoService: VideoService,
    private commentService: CommentService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const videoId = Number(params.get('id'));
      if (videoId) {
        this.loadVideo(videoId);
      }
    });
  }

  loadVideo(id: number): void {
    this.isLoading = true;
    this.videoService.getVideoById(id).subscribe({
      next: (video) => {
        this.video = video;
        this.videoService.incrementViews(id).subscribe();
        this.loadComments(id);
        this.loadRelatedVideos();
      },
      error: (error) => {
        console.error('Error loading video:', error);
        this.isLoading = false;
      }
    });
  }

  loadComments(videoId: number): void {
    this.commentService.getCommentsByVideoId(videoId).subscribe({
      next: (comments) => {
        this.comments = comments;
      },
      error: (error) => {
        console.error('Error loading comments:', error);
      }
    });
  }

  loadRelatedVideos(): void {
    this.videoService.getLatestVideos().subscribe({
      next: (videos) => {
        this.relatedVideos = videos
          .filter(v => v.id !== this.video?.id)
          .slice(0, 10);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading related videos:', error);
        this.isLoading = false;
      }
    });
  }

  likeVideo(): void {
    if (this.video?.id) {
      this.videoService.likeVideo(this.video.id).subscribe({
        next: (updatedVideo) => {
          this.video = updatedVideo;
        },
        error: (error) => {
          console.error('Error liking video:', error);
        }
      });
    }
  }

  dislikeVideo(): void {
    if (this.video?.id) {
      this.videoService.dislikeVideo(this.video.id).subscribe({
        next: (updatedVideo) => {
          this.video = updatedVideo;
        },
        error: (error) => {
          console.error('Error disliking video:', error);
        }
      });
    }
  }

  submitComment(): void {
    if (this.newComment.trim() && this.video?.id) {
      const comment: Comment = {
        content: this.newComment.trim(),
        videoId: this.video.id
      };
      
      this.commentService.createComment(comment, 1, this.video.id).subscribe({
        next: (createdComment) => {
          this.comments.unshift(createdComment);
          this.newComment = '';
        },
        error: (error) => {
          console.error('Error creating comment:', error);
        }
      });
    }
  }

  getFormattedDate(date?: Date): string {
    if (!date) return '';
    return new Date(date).toLocaleDateString();
  }

  getFormattedViews(views?: number): string {
    if (!views) return '0 views';
    if (views < 1000) return `${views} views`;
    if (views < 1000000) return `${(views / 1000).toFixed(1)}K views`;
    return `${(views / 1000000).toFixed(1)}M views`;
  }
}
