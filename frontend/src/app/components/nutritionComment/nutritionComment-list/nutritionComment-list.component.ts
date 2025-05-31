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
  loggedUserId: number = 0;
  admin: boolean = false;

  // Pagination
  page: number = 0;
  pageSize: number = 10;
  allLoaded: boolean = false;

  constructor(
    private nutritionService: NutritionService,
    private nutritionCommentService: NutritionCommentService,
    private loginService: LoginService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loginService.isLogged.subscribe((isLogged) => {
      this.logged = isLogged;
    });
    this.loginService.isAdmin.subscribe((isAdmin)=>{
      this.admin = isAdmin;
    }); // Activa cuando tengas isAdmin implementado
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
        console.error('Error loading nutrition comment', err);
        this.toastr.error('Error loading nutrition comment details', 'Error');
      }
    });
  }

  loadComments(id: number): void {
    if (this.allLoaded) return; //Avoid loading more if all comments are loaded
    
    this.nutritionCommentService.getNutritionComments(id, this.page).subscribe({
      next: (data) => {
        if (data.length < this.pageSize) {
            this.allLoaded = true;
        }
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
    if (confirm('Are you sure you want to delete this comment?')) {
      this.nutritionCommentService.deleteNutritionComment(commentId).subscribe({
        next: () => {
          this.comments = this.comments.filter(c => c.id !== commentId);
          this.toastr.success('Comment deleted');
        },
        error: () => {
          this.toastr.error('Could not delete comment');
        }
      });
    }
  }

  report(commentId: number): void {
    if (confirm('Are you sure you want to report this comment?')) {
        this.nutritionCommentService.reportNutritionComment(commentId).subscribe({
        next: () => {
            this.toastr.success('Comment reported');
        },
        error: () => {
        this.toastr.error('Could not report comment');
        }
    });
    } 
} 

  goBack(): void {
    this.router.navigate(['/nutrition']);
  }
}
