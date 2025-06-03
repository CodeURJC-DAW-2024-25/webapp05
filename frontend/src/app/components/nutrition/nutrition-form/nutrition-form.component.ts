import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NutritionService } from '../../../services/nutrition.service';
import { Nutrition } from '../../../dto/nutrition.dto';
import { LoginService } from '../../../services/login.service';

@Component({
  selector: 'app-nutrition-form',
  templateUrl: './nutrition-form.component.html',
})
export class NutritionFormComponent implements OnInit {
  nutritionForm!: FormGroup;
  nutritionId!: number;
  isEditMode = false;
  isLoading = false;
  calories: string[] = ['100', '200', '300', '400', '500'];
  goals: string[] = ['Lose weight', 'Maintain weight', 'Increase weight'];
  previewImage: string | ArrayBuffer | null = null;
  selectedImageFile?: File;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private nutritionService: NutritionService,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.nutritionForm = this.fb.group({
        name: ['', Validators.required],
        calories: [0, [Validators.required, Validators.min(1)]],
        goal: ['', Validators.required],
        description: ['', Validators.required],
      });

      const idParam = this.route.snapshot.paramMap.get('id');
      if (idParam) {
        this.nutritionId = parseInt(idParam, 10);
        if(this.nutritionId > 0) {
          this.isEditMode = true;

          this.nutritionService.getNutritionById(this.nutritionId).subscribe({
            next: (nutrition) => {
              this.nutritionForm.patchValue(nutrition);
            },
            error: () => {
              console.error('Error loading nutrition');
            }
          });
        }
      }
      this.isLoading = false;
    }




  onSubmit(): void {
    if (this.nutritionForm.invalid) {
      this.nutritionForm.markAllAsTouched();
      return;
    }

    const nutrition: Nutrition = this.nutritionForm.value;
    const updateData$ = this.isEditMode
    ? this.nutritionService.updateNutrition(this.nutritionId, nutrition)
    : this.nutritionService.createNutrition(nutrition);
  
  if (!this.nutritionForm.dirty && this.selectedImageFile && this.isEditMode) {
    this.uploadImageOnly(); 
    return;
  }

  updateData$.subscribe({
    next: (response) => {
      const id = this.isEditMode ? this.nutritionId : response.id;

      if (this.selectedImageFile) {
        this.uploadImage(id);
      } else {
        this.router.navigate(['/nutrition', id]);
      }
    },
    error: (err) => {
      console.error('Error saving nutrition', err);
    }
  });
}

  get formTitle(): string {
    return this.isEditMode ? `Edit the diet ${this.nutritionId}` : 'Create new diet';
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
    this.nutritionService.uploadNutritionImage(id, this.selectedImageFile!).subscribe({
      next: () => this.router.navigate(['/nutrition', id]),
      error: (err) => console.error('Error uploading image', err)
    });
  }

  uploadImageOnly() {
    this.uploadImage(this.nutritionId);
  }
}



