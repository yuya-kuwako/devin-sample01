import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../material.module';
import { Comment } from '../../models/comment';
import { CommentService } from '../../services/comment.service';

@Component({
  selector: 'app-comment-section',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule],
  templateUrl: './comment-section.component.html',
  styleUrl: './comment-section.component.scss'
})
export class CommentSectionComponent {
  @Input() comments: Comment[] = [];

  constructor(private commentService: CommentService) {}

  likeComment(comment: Comment): void {
    if (comment.id) {
      this.commentService.likeComment(comment.id).subscribe({
        next: (updatedComment) => {
          const index = this.comments.findIndex(c => c.id === comment.id);
          if (index !== -1) {
            this.comments[index] = updatedComment;
          }
        },
        error: (error) => {
          console.error('Error liking comment:', error);
        }
      });
    }
  }

  dislikeComment(comment: Comment): void {
    if (comment.id) {
      this.commentService.dislikeComment(comment.id).subscribe({
        next: (updatedComment) => {
          const index = this.comments.findIndex(c => c.id === comment.id);
          if (index !== -1) {
            this.comments[index] = updatedComment;
          }
        },
        error: (error) => {
          console.error('Error disliking comment:', error);
        }
      });
    }
  }

  getFormattedDate(date?: Date): string {
    if (!date) return '';
    return new Date(date).toLocaleDateString();
  }
}
