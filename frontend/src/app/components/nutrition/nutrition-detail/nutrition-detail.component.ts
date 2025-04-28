import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NutritionService } from '../../../services/nutrition.service';
import { Nutrition } from '../../../dto/nutrition.dto';
import { LoginService } from '../../../services/login.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-nutrition-detail',
  templateUrl: './nutrition-detail.component.html'
})
export class NutritionDetailComponent implements OnInit {
  nutrition: Nutrition | null = null;
  nutritionId!: number;
  canEdit: boolean = false;
  canDelete: boolean = false;
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
    this.loadNutritionDetails();
  }

  loadNutritionDetails(): void {
    this.nutritionId = Number(this.route.snapshot.paramMap.get('id'));

    this.nutritionService.getNutritionById(this.nutritionId).subscribe({
      next: (nutrition) => {
        this.isLoading = false;
        this.nutrition = nutrition;
        this.setPermissions(nutrition);
        this.checkSubscription();
      },
      error: (err) => {
        console.error('Error loading nutrition', err);
        this.toastr.error('Error loading nutrition details', 'Error');
      }
    });
  }

  private setPermissions(nutrition: Nutrition): void {
    this.loginService.currentUser.subscribe({
      next: (user) => {
        this.loginService.isAdmin.subscribe({
          next: (isAdmin) => {
            this.admin = isAdmin || false;
          },
          error: (err) => {
            console.error('Error checking admin status', err);
            this.admin = false;
          }
        });
        this.loginService.isLogged.subscribe({
          next: (isLogged) => {
            this.logged = isLogged;
          },
          error: (err) => {
            console.error('Error checking logged status', err);
            this.logged = false;
          }
        });
        this.loginService.isAdmin.subscribe({
          next: (isAdmin) => {
            this.canDelete = isAdmin || false;
          },
          error: (err) => {
            console.error('Error checking admin status', err);
            this.canDelete = false;
          }
        });
        this.loginService.isAdmin.subscribe({
          next: (isAdmin) => {
            this.canEdit = isAdmin || (user?.id !== undefined && user?.id === nutrition.user?.id);
          },
          error: (err) => {
            console.error('Error checking admin status', err);
            this.canEdit = user?.id !== undefined && user?.id === nutrition.user?.id;
          }
          });
        },
        error: (err) => {
          console.error('Error getting current user', err);
        }
      });
    }

  private checkSubscription(): void {
    if (this.loginService.isLogged) {
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
        this.router.navigate(['/nutritions']);
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
