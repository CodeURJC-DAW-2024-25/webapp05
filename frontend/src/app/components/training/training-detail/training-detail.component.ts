import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TrainingService } from '../../../services/training.service';
import { Training } from '../../../dto/training.dto';
import { LoginService } from '../../../services/login.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-training-detail',
  templateUrl: './training-detail.component.html'
})
export class TrainingDetailComponent implements OnInit {
  training: Training | null = null;
  trainingId!: number;
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
    private trainingService: TrainingService,
    private loginService: LoginService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadTrainingDetails();
  }

  loadTrainingDetails(): void {
    this.trainingId = Number(this.route.snapshot.paramMap.get('id'));

    this.trainingService.getTrainingById(this.trainingId).subscribe({
      next: (training) => {
        this.isLoading = false;
        this.training = training;
        this.setPermissions(training);
        this.checkSubscription();
      },
      error: (err) => {
        console.error('Error loading training', err);
        this.toastr.error('Error loading training details', 'Error');
      }
    });
  }

  private setPermissions(training: Training): void {
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
              this.canEdit = isAdmin || (user?.id !== undefined && user?.id === training.user?.id);
            },
            error: (err) => {
              console.error('Error checking admin status', err);
              this.canEdit = user?.id !== undefined && user?.id === training.user?.id;
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
      this.trainingService.isSubscribed(this.trainingId).subscribe({
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
    this.trainingService.subscribeToTraining(this.trainingId).subscribe({
      next: () => {
        this.subscribed = true;
        this.toastr.success('Successfully subscribed to training', 'Success');
      },
      error: (err) => {
        console.error('Error subscribing:', err);
        this.toastr.error('Error subscribing to training', 'Error');
      }
    });
  }

  onUnsubscribe(): void {
    this.trainingService.unsubscribeFromTraining(this.trainingId).subscribe({
      next: () => {
        this.subscribed = false;
        this.toastr.success('Successfully unsubscribed from training', 'Success');
      },
      error: (err) => {
        console.error('Error unsubscribing:', err);
        this.toastr.error('Error unsubscribing from training', 'Error');
      }
    });
  }

  requestDelete(): void {
    this.showConfirmationModal = true;
  }

  onDeleteConfirmed(): void {
    this.showConfirmationModal = false;
    this.isLoading = true;

    this.trainingService.deleteTraining(this.trainingId).subscribe({
      next: () => {
        this.toastr.success('Training deleted successfully', 'Success');
        this.router.navigate(['/trainings']);
      },
      error: (err) => {
        console.error('Error deleting training:', err);
        this.toastr.error('Error deleting training', 'Error');
        this.isLoading = false;
      }
    });
  }

  goToEdit(): void {
    this.router.navigate(['/training/edit', this.trainingId]);
  }

  goToComments(): void {
    this.router.navigate(['/trainingComments', this.trainingId]);
  }

  goToAddComment(): void {
    this.router.navigate([`/trainingComments/${this.trainingId}/newComment`]);
  }
}
