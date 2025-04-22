import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Training } from '../../../dto/training.dto';
import { TrainingService } from '../../../services/training.service';
import { LoginService } from '../../../services/login.service';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-training-list',
  templateUrl: './training-list.component.html'
})
export class TrainingListComponent implements OnInit {
  trainings: Training[] = [];
  logged: boolean = false;

  // Paginación
  page: number = 0;
  pageSize: number = 6;
  allLoaded: boolean = false;

  constructor(

    private trainingService: TrainingService,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.logged = this.loginService.isLogged();
    this.trainingService.getAllTrainings().subscribe({
      next: (res: Training[]) => {

        this.trainings = res;

      },
      error: (err) => {
        console.error('Error loading trainings', err);
      }
    });
    this.logged = this.loginService.isLogged();
  }

  loadCards(): void {
    if (this.allLoaded) return;

    /*this.trainingService.getTrainingsPaginated(this.page, this.pageSize).subscribe({
      next: (res: Training[]) => {
        if (res.length < this.pageSize) {
          this.allLoaded = true; // No hay más para cargar
        }
        this.trainings = [...this.trainings, ...res]; // Concatenar
        this.page++; // Siguiente página para próxima llamada
      },
      error: (err) => {
        console.error('Error loading more trainings', err);
      }
    });*/
    this.trainingService.getAllTrainings().subscribe({
      next: (res: Training[]) => {
        this.trainings = res;

      },
      error: (err) => {
        console.error('Error loading trainings', err);
      }
    });
  }

  goToRoutine(trainingId: number): void {
    this.router.navigate(['/training', trainingId]);
  }
}
