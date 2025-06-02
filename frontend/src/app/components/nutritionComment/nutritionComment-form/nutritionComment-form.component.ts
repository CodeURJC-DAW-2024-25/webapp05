import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NutritionCommentService } from '../../../services/nutritionComment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NutritionCommentDTO } from '../../../dto/nutrition-comment.dto';
import { LoginService } from '../../../services/login.service';
import { Nutrition } from '../../../dto/nutrition.dto';
import { NutritionService } from '../../../services/nutrition.service';
import { UserService } from '../../../services/user.service';
import { UserDTO } from '../../../dto/user.dto';


@Component({
  selector: 'app-nutrition-comment-form',
  templateUrl: './nutritionComment-form.component.html',
})
export class NutritionCommentFormComponent implements OnInit {
  commentForm!: FormGroup;
  nutritionId!: number;
  commentId!: number;
  isEditMode = false;
  isLoading = false;
  logged: boolean = false;
  admin: boolean = false;
  currentNutrition!: Nutrition;
  currentUser!: UserDTO;


  constructor(
    private fb: FormBuilder,
    private nutritionCommentService: NutritionCommentService,
    private route: ActivatedRoute,
    private router: Router,
    private loginService: LoginService,
    private nutritionService: NutritionService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.isLoading = true;

    // Check if the user is logged in
    this.loginService.isLogged.subscribe((isLogged) => {
      this.logged = isLogged;
    });
    this.loginService.isAdmin.subscribe((isAdmin) => {
      this.admin = isAdmin;
    });

    if (!this.logged) {
      this.router.navigate(['/login']);
      return;
    }


    this.nutritionId = +this.route.snapshot.paramMap.get('id')!;
    this.nutritionService.getNutritionById(this.nutritionId).subscribe({
      next: (nutrition) => {
        this.currentNutrition = nutrition;
      },
      error: () => {
        console.error('Error loading nutrition');
      }
    });

    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.currentUser = user;
      }
      ,
      error: () => {
        console.error('Error loading current user');
      }
    });

    const idParam = this.route.snapshot.paramMap.get('commentId');
    this.commentForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
    });

    if (idParam) {
      this.commentId = parseInt(idParam, 10);
      this.isEditMode = true;

      this.nutritionCommentService.getNutritionCommentById(this.commentId).subscribe({
        next: (nutritionComment) => {
          this.commentForm.patchValue(nutritionComment);
        },
        error: () => {
          console.error('Error loading nutrition comment');
        }
      });
    }
    this.isLoading = false;
  }

  onSubmit(): void {
    if (this.commentForm.invalid) {
      this.commentForm.markAllAsTouched();
      return;
    }

    const formValues = this.commentForm.value;
    
    const nutritionComment: NutritionCommentDTO = {
      ...formValues,
      isNotified: false,
      nutrition: this.currentNutrition,
      user: this.currentUser
    };


    if (this.isEditMode && this.commentId !== null) {
      this.nutritionCommentService.updateNutritionComment(this.commentId, nutritionComment).subscribe({
        next: () => {
          this.router.navigate(['/nutritionComments', this.nutritionId]);
        },
        error: (err) => {
          console.error('Error updating nutrition comment', err);
        }
      });
    } else {
      this.nutritionCommentService.createComment(nutritionComment).subscribe({
        next: (created) => {
          this.router.navigate(['/nutritionComments', this.nutritionId]);
        },
        error: (err) => {
          console.error('Error creating nutrition comment', err);
        }
      });
    }
    if (this.commentForm.valid) {
      const comment = {
        ...this.commentForm.value,
        isNotified: false,
        nutritionId: this.nutritionId
      };
      this.nutritionCommentService.createComment(comment).subscribe(() => {
        this.router.navigate(['/nutritionComments', this.nutritionId]);
      });
    }
  }


  onCancel(): void {
    this.router.navigate(['/nutritionComments', this.nutritionId]);
  }
}
