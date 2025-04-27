import { User } from './user';

export interface Video {
  id?: number;
  title: string;
  description?: string;
  videoUrl: string;
  thumbnailUrl?: string;
  views?: number;
  likes?: number;
  dislikes?: number;
  uploadedAt?: Date;
  user?: User;
  tags?: string[];
  commentCount?: number;
}
