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
  
    // Resetear el array de entrenamientos si es la primera carga o si estamos cargando más
    if (this.page === 0) {
      this.trainings = []; // Limpia la lista si es la primera vez que se cargan datos
    }
  
    // Enviar solo page al backend (el tamaño debería ser el predeterminado en el backend)
    this.trainingService.getTrainings(this.page).subscribe({
      next: (data) => {
        if (data.length < this.pageSize) {
          this.allLoaded = true;
        }
        this.trainings = [...this.trainings, ...data];  // Agregar los entrenamientos cargados
        this.page++;  // Incrementar la página para la siguiente carga
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
