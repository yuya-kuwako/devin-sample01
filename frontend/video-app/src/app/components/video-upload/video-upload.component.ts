import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from '../../material.module';
import { VideoService } from '../../services/video.service';
import { HeaderComponent } from '../header/header.component';
import { Video } from '../../models/video';

@Component({
  selector: 'app-video-upload',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, MaterialModule, HeaderComponent],
  templateUrl: './video-upload.component.html',
  styleUrl: './video-upload.component.scss'
})
export class VideoUploadComponent {
  video: Video = {
    title: '',
    videoUrl: '',
    description: '',
    tags: []
  };
  
  videoFile: File | null = null;
  thumbnailFile: File | null = null;
  isUploading: boolean = false;
  uploadProgress: number = 0;
  errorMessage: string = '';
  tagInput: string = '';
  
  constructor(
    private videoService: VideoService,
    private router: Router
  ) {}
  
  onVideoFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.videoFile = input.files[0];
    }
  }
  
  onThumbnailFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.thumbnailFile = input.files[0];
    }
  }
  
  addTag(): void {
    if (this.tagInput.trim()) {
      if (!this.video.tags) {
        this.video.tags = [];
      }
      this.video.tags.push(this.tagInput.trim());
      this.tagInput = '';
    }
  }
  
  removeTag(tag: string): void {
    if (this.video.tags) {
      this.video.tags = this.video.tags.filter(t => t !== tag);
    }
  }
  
  uploadVideo(): void {
    if (!this.videoFile) {
      this.errorMessage = 'Please select a video file to upload.';
      return;
    }
    
    if (!this.video.title.trim()) {
      this.errorMessage = 'Please enter a title for your video.';
      return;
    }
    
    this.isUploading = true;
    this.errorMessage = '';
    
    this.videoService.uploadVideo(this.videoFile).subscribe({
      next: (videoUrl) => {
        this.video.videoUrl = videoUrl;
        this.uploadProgress = 50;
        
        if (this.thumbnailFile) {
          this.videoService.uploadThumbnail(this.thumbnailFile).subscribe({
            next: (thumbnailUrl) => {
              this.video.thumbnailUrl = thumbnailUrl;
              this.uploadProgress = 75;
              this.createVideoEntry();
            },
            error: (error) => {
              console.error('Error uploading thumbnail:', error);
              this.errorMessage = 'Failed to upload thumbnail. Please try again.';
              this.isUploading = false;
            }
          });
        } else {
          this.uploadProgress = 75;
          this.createVideoEntry();
        }
      },
      error: (error) => {
        console.error('Error uploading video:', error);
        this.errorMessage = 'Failed to upload video. Please try again.';
        this.isUploading = false;
      }
    });
  }
  
  private createVideoEntry(): void {
    this.videoService.createVideo(this.video, 1).subscribe({
      next: (createdVideo) => {
        this.uploadProgress = 100;
        this.isUploading = false;
        
        setTimeout(() => {
          this.router.navigate(['/video', createdVideo.id]);
        }, 1000);
      },
      error: (error) => {
        console.error('Error creating video entry:', error);
        this.errorMessage = 'Failed to create video entry. Please try again.';
        this.isUploading = false;
      }
    });
  }
}
