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

    if (this.isEditMode && this.nutritionId !== null) {
      this.nutritionService.updateNutrition(this.nutritionId, nutrition).subscribe({
        next: () => {
          this.router.navigate(['/nutrition', this.nutritionId]);
        },
        error: (err) => {
          console.error('Error updating nutrition', err);
        }
      });
    } else {
      this.nutritionService.createNutrition(nutrition).subscribe({
        next: (created) => {
          this.router.navigate(['/nutrition', created.id]);
        },
        error: (err) => {
          console.error('Error creating nutrition', err);
        }
      });
    }
  }
  get formTitle(): string {
    return this.isEditMode ? `Edit the diet ${this.nutritionId}` : 'Create new diet';
  }

  onImageSelected(event: Event) {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.previewImage = reader.result;
      };
      reader.readAsDataURL(file);
      this.nutritionForm.patchValue({ imageField: file });
    }
  }
}
