import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TrainingService } from '../../../services/training.service';
import { LoginService } from '../../../services/login.service';
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
  selectedImageFile?: File;
  defaultImage: string = '/assets/images/emptyImage.png';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private trainingService: TrainingService,
    private loginService: LoginService,
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

    if (!this.isEditMode) {
      this.previewImage = this.defaultImage;
    }
    this.isLoading = false;
  }


  onSubmit(): void {
    if (this.trainingForm.invalid) {
      this.trainingForm.markAllAsTouched();
      return;
    }

    const training: Training = this.trainingForm.value;
    const updateData$ = this.isEditMode
      ? this.trainingService.updateTraining(this.trainingId, training)
      : this.trainingService.createTraining(training);

    if (!this.trainingForm.dirty && this.selectedImageFile) {
      this.uploadImageOnly(); // solo imagen
      return;
    }

    updateData$.subscribe({
      next: (response) => {
        const id = this.isEditMode ? this.trainingId : response.id;

        if (this.selectedImageFile) {
          this.uploadImage(id);
        } else {
          this.router.navigate(['/training', id]);
        }
      },
      error: (err) => {
        console.error('Error saving training', err);
      }
    });
  }

  get formTitle(): string {
    return this.isEditMode ? `Edit the routine ${this.trainingId}` : 'Create new routine';
  }

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.selectedImageFile = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.previewImage = reader.result as string;
      };
      reader.readAsDataURL(this.selectedImageFile);
    }
  }

  uploadImage(id: number) {
    this.trainingService.uploadTrainingImage(id, this.selectedImageFile!).subscribe({
      next: () => this.router.navigate(['/training', id]),
      error: (err) => console.error('Error uploading image', err)
    });
  }

  uploadImageOnly() {
    this.uploadImage(this.trainingId);
  }
}
