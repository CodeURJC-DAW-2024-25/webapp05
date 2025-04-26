import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TrainingService } from '../../../services/training.service';
import { Training } from '../../../dto/training.dto';

@Component({
  selector: 'app-training-form',
  templateUrl: './training-form.component.html',
})
export class TrainingFormComponent implements OnInit {
  trainingForm!: FormGroup;
  trainingId!: number ;
  isEditMode = false;
  isLoading = false;
  intensities: string[] = ['50%', '60%', '70%', '80%', '100%'];
  goals: string[] = ['Lose weight', 'Maintain weight', 'Increase weight'];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService
  ) {}

  ngOnInit(): void {
    this.trainingForm = this.fb.group({
      name: ['', Validators.required],
      intensity: ['', Validators.required],
      duration: [0, [Validators.required, Validators.min(1)]],
      goal: ['', Validators.required],
      description: ['', Validators.required],
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam !== null && !isNaN(+idParam)) {
      this.trainingId = parseInt(idParam, 10);
      this.isEditMode = true;

      this.isLoading = true;
      this.trainingService.getTrainingById(this.trainingId).subscribe({
        next: (training) => {
          this.trainingForm.patchValue(training);
          this.isLoading = false;
        },
        error: () => {
          console.error('Error loading training');
          this.isLoading = false;
        }
      });
    }else{
      this.isEditMode = false;
    }
  }


  onSubmit(): void {
    if (this.trainingForm.invalid) {
      this.trainingForm.markAllAsTouched();
      return;
    }

    const training: Training = this.trainingForm.value;

    if (this.isEditMode && this.trainingId !== null) {
      this.trainingService.updateTraining(this.trainingId, training).subscribe({
        next: () => {
          this.router.navigate(['/training', this.trainingId]);
        },
        error: (err) => {
          console.error('Error updating training', err);
        }
      });
    } else {
      this.trainingService.createTraining(training).subscribe({
        next: (created) => {
          this.router.navigate(['/training', created.id]);
        },
        error: (err) => {
          console.error('Error creating training', err);
        }
      });
    }
  }

  get formTitle(): string {
    return this.isEditMode ? `Edit the routine ${this.trainingId}` : 'Create new routine';
  }
}
