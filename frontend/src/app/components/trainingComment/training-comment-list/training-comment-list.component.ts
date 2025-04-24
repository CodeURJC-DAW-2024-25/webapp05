import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Training } from '../../../dto/training.dto';
import { TrainingService } from '../../../services/training.service';
import { TrainingCommentDTO } from '../../../dto/training-comment.dto';
import { TrainingCommentService } from '../../../services/trainingcomment.service';
import { LoginService } from '../../../services/login.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-training-comment-list',
  templateUrl: './training-comment-list.component.html',
})
export class TrainingCommentListComponent implements OnInit {

  comments: TrainingCommentDTO[] = [];
  training!: Training;
  trainingId!: number;
  logged: boolean = false;
  admin: boolean = false;

  // Pagination
  page: number = 0;
  pageSize: number = 10;
  allLoaded: boolean = false;

  constructor(
    private trainingService: TrainingService,
    private trainingCommentService: TrainingCommentService,
    private loginService: LoginService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.logged = this.loginService.isLogged();
    //this.admin = this.loginService.isAdmin();  FALTA ARREGLAR EL METODO isAdmin() EN login.service.ts
    this.trainingId = Number(this.route.snapshot.paramMap.get('id'));

    this.loadTraining(this.trainingId);
    this.loadComments(this.trainingId);
  }

  loadTraining(id: number): void {
    this.trainingService.getTrainingById(this.trainingId).subscribe({
        next: (training) => {
          this.training = training;
        },
        error: (err) => {
          console.error('Error loading training', err);
          this.toastr.error('Error loading training details', 'Error');
        }
      });
  }

  loadComments(id: number): void {
    this.trainingCommentService.getTrainingComments(this.trainingId, this.page).subscribe({
      next: (data) => {
        this.comments = [...this.comments, ...data];
        this.page++;
      },
      error: (err) => {
        this.toastr.error('Could not load comments');
      }
    });
    
    this.page += 1;
  }

  loadMoreComments(): void {
    this.loadComments(this.trainingId);
  }

  deleteComment(commentId: number): void {
    /*if (confirm('Are you sure you want to delete this comment?')) {
      this.trainingService.deleteComment(this.trainingId, commentId).subscribe({
        next: () => {
          this.comments = this.comments.filter(c => c.id !== commentId);
          this.toastr.success('Comment deleted');
        },
        error: () => {
          this.toastr.error('Could not delete comment');
        }
      });
    }*/
  }

  goBack(): void {
    this.router.navigate(['/training']);
  }
}