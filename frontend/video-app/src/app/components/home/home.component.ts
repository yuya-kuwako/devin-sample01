import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { MaterialModule } from '../../material.module';
import { VideoService } from '../../services/video.service';
import { Video } from '../../models/video';
import { HeaderComponent } from '../header/header.component';
import { VideoCardComponent } from '../video-card/video-card.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule, HeaderComponent, VideoCardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  videos: Video[] = [];
  latestVideos: Video[] = [];
  trendingVideos: Video[] = [];
  isLoading: boolean = true;
  searchQuery: string | null = null;

  constructor(
    private videoService: VideoService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.searchQuery = params['search'];
      this.loadVideos();
    });
  }

  loadVideos(): void {
    this.isLoading = true;
    
    if (this.searchQuery) {
      this.videoService.searchVideos(this.searchQuery).subscribe({
        next: (videos) => {
          this.videos = videos;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error searching videos:', error);
          this.isLoading = false;
        }
      });
    } else {
      this.videoService.getLatestVideos().subscribe({
        next: (videos) => {
          this.latestVideos = videos;
          this.loadTrendingVideos();
        },
        error: (error) => {
          console.error('Error loading latest videos:', error);
          this.isLoading = false;
        }
      });
    }
  }

  loadTrendingVideos(): void {
    this.videoService.getTrendingVideos().subscribe({
      next: (videos) => {
        this.trendingVideos = videos;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading trending videos:', error);
        this.isLoading = false;
      }
    });
  }
}
