import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { VideoPlayerComponent } from './components/video-player/video-player.component';
import { VideoUploadComponent } from './components/video-upload/video-upload.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'video/:id', component: VideoPlayerComponent },
  { path: 'upload', component: VideoUploadComponent },
  { path: 'user/:id', component: UserProfileComponent },
  { path: '**', redirectTo: '' }
];
