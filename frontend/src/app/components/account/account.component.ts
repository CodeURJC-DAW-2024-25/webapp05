import {
  Component,
  OnInit
} from '@angular/core';
import { LoginService } from '../../services/login.service';
import { UserService } from '../../services/user.service';
import { UserDTO } from '../../dto/user.dto';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { of } from 'rxjs';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
})
export class AccountComponent implements OnInit {
  user: UserDTO | null = null;
  trainings: any[] = [];
  nutritions: any[] = [];
  previewUrl: string | ArrayBuffer | null = null;
  selectedFile?: File;

  constructor(
    private loginService: LoginService,
    private userService: UserService,
    private toastr: ToastrService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    const currentUser = this.loginService.getCurrentUserValue();
    if (!currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.user = user;
        this.loadUserPlans();
      },
      error: (err) => {
        this.toastr.error('Authentication required. Please log in.', 'Error');
        this.router.navigate(['/login']);
      }
    });
  }


  loadUserPlans() {
    this.http
      .get<any[]>(`/api/users/trainingList`)
      .subscribe((data) => (this.trainings = data));
    this.http
      .get<any[]>(`/api/users/nutritionList`)
      .subscribe((data) => (this.nutritions = data));
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];

      const reader = new FileReader();
      reader.onload = (e) => (this.previewUrl = reader.result);
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onSubmit(event: Event, form: NgForm): void {
    event.preventDefault();

    if (!form.valid || !this.user) {
    this.toastr.error('Please complete the form correctly.', 'Error');
    return;
  }

    if (!this.user) return;

    const formElement = event.target as HTMLFormElement;

    const formData = new FormData(formElement);

    const name = (formData.get('name') as string).trim();
    const email = (formData.get('email') as string).trim();

    const updateUserData: Partial<UserDTO> = {
      name,
      email,
    };

    const originalUser = this.loginService.getCurrentUserValue();

    // Check if the user has changed their name or email
    const nameChanged = name !== originalUser?.name;
    const emailChanged = email !== originalUser?.email;
    const hasDataChanged = nameChanged || emailChanged;

    // Check if the user has selected a new image file
    const hasImageChanged = !!this.selectedFile;

    if (!hasDataChanged && !hasImageChanged) {
      this.toastr.info('No changes to save', 'Info');
      return;
    }

    let updateUser$: any = of(null);

    if (hasDataChanged) {
      updateUser$ = this.userService.updateUser(this.user.id, updateUserData);
    } else {
      updateUser$ = of(null);
    }

    updateUser$.subscribe({
    next: () => {
      if (hasImageChanged) {
        this.uploadImage(this.user!.id);
      } else if (emailChanged) {
        this.toastr.success('Email changed successfully, please log in again.', 'Success');
        this.loginService.logout().subscribe(() => {
          this.router.navigate(['/login']);
        });
      } else if (nameChanged) {
        this.toastr.success('User data updated successfully', 'Success');

        // Refresh the user data
        this.userService.getCurrentUser().subscribe((updatedUser) => {
          this.user = updatedUser;
          this.loginService.updateCurrentUser(updatedUser);
        });
      }
    },
    error: (err: any) => {
      const msg = err?.error?.message || 'Error updating user data';
      this.toastr.error(msg, 'Error');
      console.error(err);
    },
  });

  }

  uploadImage(userId: number): void {
    if (!this.selectedFile) return;

    this.userService.updateUserImage(userId, this.selectedFile).subscribe({
      next: () => {
        this.toastr.success('Image uploaded successfully', 'Success');
        this.previewUrl = null;
        this.selectedFile = undefined;
      },
      error: (err) => {
        this.toastr.error('Error uploading image', 'Error');
        console.error(err);
      },
    });
  }

  deleteTraining(id: number) {
    this.userService.unsubscribeFromTraining(id).subscribe({
      next: () => {
        this.trainings = this.trainings.filter(
          (training) => training.id !== id
        );
        this.toastr.success(
          'Successfully unsubscribed from training',
          'Success'
        );
      },
      error: (err) => {
        this.toastr.error('Error unsubscribing from training', 'Error');
      },
    });
  }

  showTraining(id: number) {
    this.router.navigate(['/training', id]);
  }

  deleteNutrition(id: number) {
    this.userService.unsubscribeFromNutrition(id).subscribe({
      next: () => {
        this.nutritions = this.nutritions.filter(
          (nutrition) => nutrition.id !== id
        );
        this.toastr.success(
          'Successfully unsubscribed from nutrition',
          'Success'
        );
      },
      error: (err) => {
        this.toastr.error('Error unsubscribing from nutrition', 'Error');
      },
    });
  }

  showNutrition(id: number) {
    this.router.navigate(['/nutrition', id]);
  }
}
