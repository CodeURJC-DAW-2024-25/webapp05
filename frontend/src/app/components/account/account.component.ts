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
    this.http.get<any[]>(`/api/users/${this.user?.id}/trainings`).subscribe(data => this.trainings = data);
    this.http.get<any[]>(`/api/users/${this.user?.id}/nutritions`).subscribe(data => this.nutritions = data);
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
    const formData = new FormData();
    if (!this.user) return;

    formData.append('id', this.user.id.toString());
    formData.append('name', this.user.username);
    formData.append('email', this.user.email);
    if (this.selectedFile) {
      formData.append('imageField', this.selectedFile);
    }

    this.http.post('/api/users/edit', formData).subscribe({
      next: () => alert('Saved successfully'),
      error: err => alert('Error saving: ' + err.message)
    });
  }

  deleteTraining(id: number) {
    this.http.delete(`/api/trainings/${id}`).subscribe(() => this.loadUserPlans());
  }

  showTraining(id: number) {
    this.router.navigate(['/training', id]);
  }

  deleteNutrition(id: number) {
    this.http.delete(`/api/nutritions/${id}`).subscribe(() => this.loadUserPlans());
  }

  showNutrition(id: number) {
    this.router.navigate(['/nutrition', id]);
  }
}
