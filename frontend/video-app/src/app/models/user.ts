export interface User {
  id?: number;
  username: string;
  email?: string;
  profilePictureUrl?: string;
  bio?: string;
  createdAt?: Date;
  videoCount?: number;
}
