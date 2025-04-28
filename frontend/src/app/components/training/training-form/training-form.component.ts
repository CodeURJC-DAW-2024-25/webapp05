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
  previewImage: string | ArrayBuffer | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.trainingForm = this.fb.group({
      name: ['', Validators.required],
      intensity: ['', Validators.required],
      duration: [0, [Validators.required, Validators.min(1)]],
      goal: ['', Validators.required],
      description: ['', Validators.required],
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.trainingId = parseInt(idParam, 10);
      if(this.trainingId > 0) {
        this.isEditMode = true;

        this.trainingService.getTrainingById(this.trainingId).subscribe({
          next: (training) => {
            this.trainingForm.patchValue(training);
          },
          error: () => {
            console.error('Error loading training');
          }
        });
      }
    }
    this.isLoading = false;
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

  onImageSelected(event: Event) {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.previewImage = reader.result;
      };
      reader.readAsDataURL(file);
      this.trainingForm.patchValue({ imageField: file });
    }
  }
}
