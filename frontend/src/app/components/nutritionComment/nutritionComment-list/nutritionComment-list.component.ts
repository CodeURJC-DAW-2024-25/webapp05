import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Nutrition } from '../../../dto/nutrition.dto';
import { NutritionService } from '../../../services/nutrition.service';
import { NutritionCommentDTO } from '../../../dto/nutrition-comment.dto';
import { NutritionCommentService } from '../../../services/nutritionComment.service';
import { LoginService } from '../../../services/login.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-nutrition-comment-list',
  templateUrl: './nutritionComment-list.component.html',
})
export class NutritionCommentListComponent implements OnInit {

  comments: NutritionCommentDTO[] = [];
  nutrition!: Nutrition;
  nutritionId!: number;
  logged: boolean = false;
  admin: boolean = false;

  // Pagination
  page: number = 0;
  pageSize: number = 10;

  constructor(
    private nutritionService: NutritionService,
    private nutritionCommentService: NutritionCommentService,
    private loginService: LoginService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.logged = this.loginService.isLogged();
    // this.admin = this.loginService.isAdmin(); // Activa cuando tengas isAdmin implementado
    this.nutritionId = Number(this.route.snapshot.paramMap.get('id'));

    this.loadNutrition(this.nutritionId);
    this.loadComments(this.nutritionId);
  }

  loadNutrition(id: number): void {
    this.nutritionService.getNutritionById(this.nutritionId).subscribe({
      next: (nutrition) => {
        this.nutrition = nutrition;
      },
      error: (err) => {
        console.error('Error loading nutrition', err);
        this.toastr.error('Error loading nutrition details', 'Error');
      }
    });
  }

  loadComments(id: number): void {
    this.nutritionCommentService.getNutritionComments(this.nutritionId, this.page).subscribe({
      next: (data) => {
        this.comments = [...this.comments, ...data];
        this.page++;
      },
      error: (err) => {
        this.toastr.error('Could not load comments');
      }
    });
  }

  loadMoreComments(): void {
    this.loadComments(this.nutritionId);
  }

  deleteComment(commentId: number): void {
    // Implementación opcional al tener permiso de admin
    /*
    if (confirm('Are you sure you want to delete this comment?')) {
      this.nutritionCommentService.deleteComment(this.nutritionId, commentId).subscribe({
        next: () => {
          this.comments = this.comments.filter(c => c.id !== commentId);
          this.toastr.success('Comment deleted');
        },
        error: () => {
          this.toastr.error('Could not delete comment');
        }
      });
    }
    */
  }

  goBack(): void {
    this.router.navigate(['/nutrition']);
  }
}
