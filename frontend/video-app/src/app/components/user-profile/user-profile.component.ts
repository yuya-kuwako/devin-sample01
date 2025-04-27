import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { MaterialModule } from '../../material.module';
import { UserService } from '../../services/user.service';
import { VideoService } from '../../services/video.service';
import { User } from '../../models/user';
import { Video } from '../../models/video';
import { HeaderComponent } from '../header/header.component';
import { VideoCardComponent } from '../video-card/video-card.component';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule, HeaderComponent, VideoCardComponent],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss'
})
export class UserProfileComponent implements OnInit {
  user: User | null = null;
  videos: Video[] = [];
  isLoading: boolean = true;
  
  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private videoService: VideoService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const userId = Number(params.get('id'));
      if (userId) {
        this.loadUserProfile(userId);
      }
    });
  }

  loadUserProfile(userId: number): void {
    this.isLoading = true;
    
    this.userService.getUserById(userId).subscribe({
      next: (user) => {
        this.user = user;
        this.loadUserVideos(userId);
      },
      error: (error) => {
        console.error('Error loading user profile:', error);
        this.isLoading = false;
      }
    });
  }

  loadUserVideos(userId: number): void {
    this.videoService.getVideosByUserId(userId).subscribe({
      next: (videos) => {
        this.videos = videos;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading user videos:', error);
        this.isLoading = false;
      }
    });
  }

  getFormattedDate(date?: Date): string {
    if (!date) return '';
    return new Date(date).toLocaleDateString();
  }
}
