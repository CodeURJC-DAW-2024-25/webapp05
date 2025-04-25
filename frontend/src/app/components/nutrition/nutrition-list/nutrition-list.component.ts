import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Nutrition } from '../../../dto/nutrition.dto';
import { NutritionService } from '../../../services/nutrition.service';
import { LoginService } from '../../../services/login.service';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-nutrition-list',
  templateUrl: './nutrition-list.component.html'
})
export class NutritionListComponent implements OnInit {
  nutritions: Nutrition[] = [];
  logged: boolean = false;

  // Paginación
  page: number = 0;
  pageSize: number = 3;
  allLoaded: boolean = false;

  constructor(

    private nutritionService: NutritionService,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.logged = this.loginService.isLogged();
    this.nutritionService.getAllNutritions().subscribe({
      next: (res: Nutrition[]) => {

        this.nutritions = res;

      },
      error: (err) => {
        console.error('Error loading nutritions', err);
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
    this.nutritionService.getAllNutritions().subscribe({
      next: (res: Nutrition[]) => {
        this.nutritions = res;

      },
      error: (err) => {
        console.error('Error loading nutritions', err);
      }
    });
  }

  goToDiet(nutritionId: number): void {
    this.router.navigate(['/nutrition', nutritionId]);
  }
}
