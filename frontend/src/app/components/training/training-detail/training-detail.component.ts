import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TrainingService } from '../../../services/training.service';
import { Training } from '../../../dto/training.dto';
import { LoginService } from '../../../services/login.service';
import { ToastrService } from 'ngx-toastr';
import { combineLatest } from 'rxjs';

@Component({
  selector: 'app-training-detail',
  templateUrl: './training-detail.component.html'
})
export class TrainingDetailComponent implements OnInit {
  training: Training | null = null;
  trainingId!: number;
  canEdit: boolean = false;
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
    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
    // Check if the user is logged in
    this.loginService.isLogged.subscribe((isLogged) => {
      this.logged = isLogged;
    });
    this.loginService.isAdmin.subscribe((isAdmin) => {
      this.admin = isAdmin;
    });

    if (!this.logged) {
      this.router.navigate(['/login']);
      return;
    }
    this.loadTrainingDetails();
  }

  loadTrainingDetails(): void {
    this.trainingId = Number(this.route.snapshot.paramMap.get('id'));

    this.trainingService.getTrainingById(this.trainingId).subscribe({
      next: (training) => {
        this.isLoading = false;
        this.training = training;
        this.setPermissions(training);
        if (this.logged && !this.admin) {
          this.checkSubscription();
        }

      },
      error: (err) => {
        console.error('Error loading training', err);
        this.toastr.error('Error loading training details', 'Error');
      }
    });
  }

  private setPermissions(training: Training): void {
    combineLatest([
      this.loginService.currentUser,
      this.loginService.isAdmin,
      this.loginService.isLogged
    ]).subscribe({
      next: ([currentUser, isAdmin, isLogged]) => {
        this.logged = isLogged;
        this.admin = isAdmin;

        const currentUserId = currentUser?.id ? Number(currentUser.id) : null;
        const trainingAuthorId = training.userId ? Number(training.userId) : null;


        const isAuthor = currentUserId !== null &&
          trainingAuthorId !== null &&
          currentUserId === trainingAuthorId;

        this.canEdit = isAdmin || isAuthor;

      },
      error: (err) => {
        console.error('Error checking permissions:', err);
        this.canEdit = false;
      }
    });
  }

  private checkSubscription(): void {
    if (this.loginService.currentUser != null) {
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
        console.error('Error subscribing to training', err);
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
        this.router.navigate(['/training']);
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
