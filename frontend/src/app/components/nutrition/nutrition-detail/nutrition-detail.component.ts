import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NutritionService } from '../../../services/nutrition.service';
import { Nutrition } from '../../../dto/nutrition.dto';
import { LoginService } from '../../../services/login.service';
import { ToastrService } from 'ngx-toastr';
import { combineLatest } from 'rxjs';

@Component({
  selector: 'app-nutrition-detail',
  templateUrl: './nutrition-detail.component.html'
})
export class NutritionDetailComponent implements OnInit {
  nutrition: Nutrition | null = null;
  nutritionId!: number;
  canEdit: boolean = false;
  subscribed: boolean = false;
  isLoading: boolean = true;
  showConfirmationModal: boolean = false;
  logged: boolean = false;
  admin: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private nutritionService: NutritionService,
    private loginService: LoginService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    // Check if the user is logged in
    this.loginService.isLogged.subscribe((isLogged) => {
        this.logged = isLogged;
      });
      this.loginService.isAdmin.subscribe((isAdmin)=>{
        this.admin = isAdmin;
      }); 

    if (!this.logged) {
      this.router.navigate(['/login']);
      return;
    }
    this.loadNutritionDetails();
  }

  loadNutritionDetails(): void {
    this.nutritionId = Number(this.route.snapshot.paramMap.get('id'));

    this.nutritionService.getNutritionById(this.nutritionId).subscribe({
      next: (nutrition) => {
        this.isLoading = false;
        this.nutrition = nutrition;
        this.setPermissions(nutrition);
        if (this.logged && !this.admin){
          this.checkSubscription();
        }
      },
      error: (err) => {
        console.error('Error loading nutrition', err);
        this.toastr.error('Error loading nutrition details', 'Error');
      }
    });
  }

  private setPermissions(nutrition: Nutrition): void {
    combineLatest([
      this.loginService.currentUser,
      this.loginService.isAdmin,
      this.loginService.isLogged
    ]).subscribe({
      next: ([currentUser, isAdmin, isLogged]) => {
        this.logged = isLogged;
        this.admin = isAdmin;

        const currentUserId = currentUser?.id ? Number(currentUser.id) : null;
        const nutritionAuthorId = nutrition.userId ? Number(nutrition.userId) : null;


        const isAuthor = currentUserId !== null &&
          nutritionAuthorId !== null &&
          currentUserId === nutritionAuthorId;

        this.canEdit = isAdmin || isAuthor;

      },
      error: (err) => {
        console.error('Error checking permissions:', err);
        this.canEdit = false;
      }
    });
  }

  private checkSubscription(): void {
    if (this.loginService.currentUser!=null) {
      this.nutritionService.isSubscribed(this.nutritionId).subscribe({
        next: (res) => {
          this.subscribed = res;
        },
        error: (err) => {
          console.error('Error checking subscription:', err);
        }
      });
    }
  }

  onSubscribe(): void {
    this.nutritionService.subscribeToNutrition(this.nutritionId).subscribe({
      next: () => {
        this.subscribed = true;
        this.toastr.success('Successfully subscribed to nutrition', 'Success');
      },
      error: (err) => {
        console.error('Error subscribing:', err);
        this.toastr.error('Error subscribing to nutrition', 'Error');
      }
    });
  }

  onUnsubscribe(): void {
    this.nutritionService.unsubscribeFromNutrition(this.nutritionId).subscribe({
      next: () => {
        this.subscribed = false;
        this.toastr.success('Successfully unsubscribed from nutrition', 'Success');
      },
      error: (err) => {
        console.error('Error unsubscribing:', err);
        this.toastr.error('Error unsubscribing from nutrition', 'Error');
      }
    });
  }

  requestDelete(): void {
    this.showConfirmationModal = true;
  }

  onDeleteConfirmed(): void {
    this.showConfirmationModal = false;
    this.isLoading = true;

    this.nutritionService.deleteNutrition(this.nutritionId).subscribe({
      next: () => {
        this.toastr.success('Nutrition deleted successfully', 'Success');
        this.router.navigate(['/nutrition']);
      },
      error: (err) => {
        console.error('Error deleting nutrition:', err);
        this.toastr.error('Error deleting nutrition', 'Error');
        this.isLoading = false;
      }
    });
  }

  goToEdit(): void {
    this.router.navigate(['/nutrition/edit', this.nutritionId]);
  }

  goToComments(): void {
    this.router.navigate(['/nutritionComments', this.nutritionId]);
  }

  goToAddComment(): void {
    this.router.navigate([`/nutritionComments/${this.nutritionId}/newComment`]);
  }
}
