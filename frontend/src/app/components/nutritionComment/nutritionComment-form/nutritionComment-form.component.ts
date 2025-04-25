import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NutritionCommentService } from '../../../services/nutritionComment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NutritionCommentDTO } from '../../../dto/nutrition-comment.dto';

@Component({
  selector: 'app-nutrition-comment-form',
  templateUrl: './nutritionComment-form.component.html',
})
export class NutritionCommentFormComponent implements OnInit {
  commentForm!: FormGroup;
  nutritionId!: number;
  isEditMode = false;

  
  constructor(
    private fb: FormBuilder,
    private commentService: NutritionCommentService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.nutritionId = +this.route.snapshot.paramMap.get('id')!;
    this.commentForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.commentForm.valid) {
      const comment = {
        ...this.commentForm.value,
        isNotified: false,
        nutritionId: this.nutritionId
      };
      this.commentService.createComment(comment).subscribe(() => {
        this.router.navigate(['/nutrition', this.nutritionId]);
      });
    }
  }


  onCancel(): void {
    this.router.navigate(['/nutrition', this.nutritionId]);
  }
}
