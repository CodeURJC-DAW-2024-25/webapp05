import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TrainingCommentService } from '../../../services/trainingcomment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TrainingCommentDTO } from '../../../dto/training-comment.dto';
import { LoginService } from '../../../services/login.service';
import { Training } from '../../../dto/training.dto';
import { TrainingService } from '../../../services/training.service';
import { UserService } from '../../../services/user.service';
import { UserDTO } from '../../../dto/user.dto';
import { ToastrService } from 'ngx-toastr';
import { of } from 'rxjs';

@Component({
  selector: 'app-training-comment-form',
  templateUrl: './training-comment-form.component.html',
})
export class TrainingCommentFormComponent implements OnInit {
  commentForm!: FormGroup;
  trainingId!: number;
  commentId!: number;
  isEditMode = false;
  isLoading = false;
  logged: boolean = false;
  admin: boolean = false;
  currentTraining!: Training;
  currentUser: UserDTO | null = null;


  constructor(
    private fb: FormBuilder,
    private trainingCommentService: TrainingCommentService,
    private route: ActivatedRoute,
    private router: Router,
    private loginService: LoginService,
    private trainingService: TrainingService,
    private userService: UserService,
    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
    this.isLoading = true;

    // Check if user is logged in
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.currentUser = user;
        this.isLoading = false;

        // Load training details
        this.trainingId = +this.route.snapshot.paramMap.get('id')!;
        this.trainingService.getTrainingById(this.trainingId).subscribe({
          next: (training) => {
            this.currentTraining = training;
          },
          error: () => {
            console.error('Error loading training');
          }
        });

        // Initialize the comment form
        this.commentForm = this.fb.group({
          name: ['', Validators.required],
          description: ['', Validators.required],
        });

        // Verify if in edit mode
        const idParam = this.route.snapshot.paramMap.get('commentId');
        if (idParam) {
          this.commentId = parseInt(idParam, 10);
          this.isEditMode = true;

          this.trainingCommentService.getTrainingCommentById(this.commentId).subscribe({
            next: (trainingComment) => {
              this.commentForm.patchValue(trainingComment);
            },
            error: () => {
              console.error('Error loading training comment');
            }
          });
        }

      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 401 || err.status === 403) {
          this.router.navigate(['/login']);
        } else {
          this.toastr.error('Please log in to access this part of web.', 'Error');
        }
      }
    });
  }


  onSubmit(): void {
    if (this.commentForm.invalid) {
      this.commentForm.markAllAsTouched();
      return;
    }

    const formValues = this.commentForm.value;

    const trainingComment: TrainingCommentDTO = {
      ...formValues,
      isNotified: false,
      training: this.currentTraining,
      user: this.currentUser
    };

    if (this.isEditMode && this.commentId !== null) {
      this.trainingCommentService.updateTrainingComment(this.commentId, trainingComment).subscribe({
        next: () => {
          this.router.navigate(['/trainingComments', this.trainingId]);
        },
        error: (err) => {
          console.error('Error updating training', err);
        }
      });
    } else {
      this.trainingCommentService.createComment(trainingComment).subscribe({
        next: (created) => {
          this.router.navigate(['/trainingComments', this.trainingId]);
        },
        error: (err) => {
          console.error('Error creating training', err);
        }
      });
    }





    if (this.commentForm.valid) {
      const comment = {
        ...this.commentForm.value,
        isNotified: false,
        trainingId: this.trainingId
      };
      this.trainingCommentService.createComment(comment).subscribe(() => {
        this.router.navigate(['/trainingComments', this.trainingId]);
      });
    }
  }


  onCancel(): void {
    this.router.navigate(['/trainingComments', this.trainingId]);
  }
}
