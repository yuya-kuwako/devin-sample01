import { User } from './user';

export interface Comment {
  id?: number;
  content: string;
  createdAt?: Date;
  likes?: number;
  dislikes?: number;
  user?: User;
  videoId?: number;
}
