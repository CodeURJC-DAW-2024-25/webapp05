import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NutritionService } from '../../../services/nutrition.service';
import { Nutrition } from '../../../dto/nutrition.dto';
import { LoginService } from '../../../services/login.service';
import { UserService } from '../../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { of } from 'rxjs';
import { UserDTO } from '../../../dto/user.dto';

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
  defaultImage: string = '/assets/images/emptyImage.png';
  user: UserDTO | null = null;
  canEdit: boolean = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private nutritionService: NutritionService,
    private userService: UserService,
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
      this.user = user;
      if (user) {
        this.loadNutrition();
        this.isLoading = false;
      // }else{
      //   this.isLoading = false;
      //   this.toastr.error('Please log in to access this part of web.', 'Error');
      //   // Redirect to login page if not logged in
      //   this.router.navigate(['/login']);
       }
      },
      error: () => {
        this.isLoading = false;
        this.toastr.error('Please log in to access this part of web.', 'Error');
        // Redirect to login page if not logged in
        this.router.navigate(['/login']);
      }
    });
  
  }

  loadNutrition(){
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

            const currentUserId = this.user?.id ?? null;
            const nutritionAuthorId = nutrition.userId ?? null;

            const isAuthor = currentUserId !== null &&
              nutritionAuthorId !== null &&
              Number(currentUserId) === Number(nutritionAuthorId);

            this.canEdit = currentUserId == 1 || isAuthor;
            if (this.canEdit) {
              this.nutritionForm.patchValue(nutrition);
              this.isLoading = false;
              return;
            } else {
              this.isLoading = false;
              this.toastr.error('You cant edit this nutrition.', 'Error');
              this.router.navigate(['/nutrition' , this.nutritionId]);
              return;
            }
          },
          error: () => {
            this.isLoading = false;
            console.error('Error loading nutrition');
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



