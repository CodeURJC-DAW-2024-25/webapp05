import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TrainingCommentService } from '../../../services/trainingcomment.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TrainingCommentDTO } from '../../../dto/training-comment.dto';

@Component({
  selector: 'app-training-comment-form',
  templateUrl: './training-comment-form.component.html',
})
export class TrainingCommentFormComponent implements OnInit {
  commentForm!: FormGroup;
  trainingId!: number;
  isEditMode = false;

  
  constructor(
    private fb: FormBuilder,
    private trainingCommentService: TrainingCommentService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.trainingId = +this.route.snapshot.paramMap.get('id')!;
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
