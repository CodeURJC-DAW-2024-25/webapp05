import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { UserDTO } from '../../dto/user.dto';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

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
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginService.currentUser.subscribe(user => {
      this.user = user;
      if (user) {
        this.loadUserPlans();
      }
    });
  }

  loadUserPlans() {
    this.http.get<any[]>(`/api/users/trainingList`).subscribe(data => this.trainings = data);
    this.http.get<any[]>(`/api/users/nutritionList`).subscribe(data => this.nutritions = data);
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];

      const reader = new FileReader();
      reader.onload = e => this.previewUrl = reader.result;
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onSubmit() {
    if (!this.user) return;

    // 1. Update user (PUT + JSON)
    this.http.put(`https://localhost:8443/api/users/${this.user.id}`, {
      id: this.user.id,
      username: this.user.name,
      email: this.user.email
    }, {
      withCredentials: true
    }).subscribe({
      next: () => {
        // 2. if image is selected, upload it (PUT + FormData)
        if (this.selectedFile) {
          const imageData = new FormData();
          imageData.append('imageFile', this.selectedFile);
          this.http.put(`cusers/${this.user?.id}/image`, imageData, {
            withCredentials: true
          }).subscribe({
            next: () => alert('Saved successfully'),
            error: err => alert('Error uploading image: ' + err.message)
          });
        } else {
          alert('Saved successfully');
        }
      },
      error: err => alert('Error saving user: ' + err.message)
    });
  }

  deleteTraining(id: number) {
    return this.http.post(`https://localhost:8443/api/trainings/unsubscribed/${id}`, { withCredentials: true });
  }

  showTraining(id: number) {
    this.router.navigate(['/training', id]);
  }

  deleteNutrition(id: number) {
    return this.http.post(`https://localhost:8443/api/nutritions/unsubscribed/${id}`, { withCredentials: true });
  }

  showNutrition(id: number) {
    this.router.navigate(['/nutrition', id]);
  }
}
