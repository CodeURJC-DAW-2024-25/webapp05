import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TrainingService } from '../../../services/training.service';
import { LoginService } from '../../../services/login.service';
import { ToastrService } from 'ngx-toastr';
import { Training } from '../../../dto/training.dto';

@Component({
  selector: 'app-training-list',
  templateUrl: './training-list.component.html',
})
export class TrainingListComponent implements OnInit {

  trainings: Training[] = [];
  page: number = 0;
  pageSize: number = 10;
  allLoaded: boolean = false;
  logged: boolean = false;

  constructor(
    private trainingService: TrainingService,
    private loginService: LoginService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.logged = this.loginService.isLogged();
    this.loadTrainings();
  }

  loadTrainings(): void {
    if (this.allLoaded) return;
  
    // Reset the training array if it is the first load or if we are loading more.
    if (this.page === 0) {
      this.trainings = []; // Clear the list if this is the first time data is uploaded.
    }
  
    // Send only page to backend (size should be the default size in the backend)
    this.trainingService.getTrainings(this.page).subscribe({
      next: (data) => {
        if (data.length < this.pageSize) {
          this.allLoaded = true;
        }
        this.trainings = [...this.trainings, ...data];  // Add uploaded trainings
        this.page++;  // Increment the page for the next load
      },
      error: (err) => {
        console.error('Error loading trainings', err);
        this.toastr.error('Could not load trainings', 'Error');
      }
    });
  }
  

  loadMoreTrainings(): void {
    this.loadTrainings();
  }

  goToRoutine(trainingId: number): void {
    this.router.navigate(['/training', trainingId]);
  }
}
