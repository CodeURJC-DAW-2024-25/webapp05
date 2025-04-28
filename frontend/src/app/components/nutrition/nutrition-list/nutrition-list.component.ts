import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Nutrition } from '../../../dto/nutrition.dto';
import { NutritionService } from '../../../services/nutrition.service';
import { LoginService } from '../../../services/login.service';
import { FormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-nutrition-list',
  templateUrl: './nutrition-list.component.html',
})
export class NutritionListComponent implements OnInit {
  nutritions: Nutrition[] = [];
  logged: boolean = false;
  page: number = 0;
  pageSize: number = 10;
  allLoaded: boolean = false;

  constructor(

    private nutritionService: NutritionService,
    private loginService: LoginService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loginService.isLogged.subscribe((isLoggedIn: boolean) => {
      this.logged = isLoggedIn;
    });
    this.loadNutritions();
  }

  loadNutritions(): void {
    if (this.allLoaded) return;

    // Reset the nutrition array if it is the first load or if we are loading more.
    if (this.page === 0) {
      this.nutritions = []; // Clear the list if this is the first time data is uploaded.
    }

    // Send only page to backend (size should be the default size in the backend)
    this.nutritionService.getNutritions(this.page).subscribe({
      next: (data) => {
        if (data.length < this.pageSize) {
          this.allLoaded = true;
        }
        this.nutritions = [...this.nutritions, ...data];  // Add uploaded nutritions
        this.page++;  // Increment the page for the next load
      },
      error: (err) => {
        console.error('Error loading nutritions', err);
        this.toastr.error('Could not load nutritions', 'Error');
      }
    });
  }


  loadMoreNutritions(): void {
    this.loadNutritions();
  }

  goToDiet(nutritionId: number): void {
    this.router.navigate(['/nutrition', nutritionId]);
  }
}
