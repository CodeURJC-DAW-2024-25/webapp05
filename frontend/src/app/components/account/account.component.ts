import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import {LoginService} from '../../services/login.service';
import {UserService} from '../../services/user.service';
import {UserDTO} from '../../dto/user.dto';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
})
export class AccountComponent implements OnInit, AfterViewInit {
  @ViewChild('chartCanvas') chartCanvas!: ElementRef;
  pieChart: any;

  user: UserDTO | null = null;
  trainings: any[] = [];
  nutritions: any[] = [];
  previewUrl: string | ArrayBuffer | null = null;
  selectedFile?: File;

  constructor(
    private loginService: LoginService,
    private userService: UserService,
    private toastr: ToastrService,
    private http: HttpClient,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.loginService.currentUser.subscribe(user => {
      this.user = user;
      if (user) {
        this.loadUserPlans();
        this.generateChart();
      }
    });
  }

  ngAfterViewInit(): void {
    // El gráfico se genera al terminar la carga de los datos
  }

  loadUserPlans() {
    this.http.get<any[]>(`/api/users/trainingList`).subscribe(data => this.trainings = data);
    this.http.get<any[]>(`/api/users/nutritionList`).subscribe(data => this.nutritions = data);
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
    if (!this.user) return;

    // 1. Update user (PUT + JSON)
    this.http.put(`https://localhost:8443/api/users/${this.user.id}`, {
      id: this.user.id,
      username: this.user.name,
      email: this.user.email
    }, {
      withCredentials: true
    }).subscribe({
      next: () => {
        // 2. if image is selected, upload it (PUT + FormData)
        if (this.selectedFile) {
          const imageData = new FormData();
          imageData.append('imageFile', this.selectedFile);
          this.http.put(`cusers/${this.user?.id}/image`, imageData, {
            withCredentials: true
          }).subscribe({
            next: () => alert('Saved successfully'),
            error: err => alert('Error uploading image: ' + err.message)
          });
        } else {
          alert('Saved successfully');
        }
      },
      error: err => alert('Error saving user: ' + err.message)
    });
  }

  deleteTraining(id: number) {
    this.userService.unsubscribeFromTraining(id).subscribe({
      next: () => {
        this.trainings = this.trainings.filter(training => training.id !== id);
        this.toastr.success('Successfully unsubscribed from training', 'Success');
      },
      error: (err) => {
        this.toastr.error('Error unsubscribing from training', 'Error');
      }
    });
  }

  showTraining(id: number) {
    this.router.navigate(['/training', id]);
  }

  deleteNutrition(id: number) {
    this.userService.unsubscribeFromNutrition(id).subscribe({
      next: () => {
        this.nutritions = this.nutritions.filter(nutrition => nutrition.id !== id);
        this.toastr.success('Successfully unsubscribed from nutrition', 'Success');
      },
      error: (err) => {
        this.toastr.error('Error unsubscribing from nutrition', 'Error');
      }
    });
  }

  showNutrition(id: number) {
    this.router.navigate(['/nutrition', id]);
  }

  generateChart() {
  this.userService.reportedComments().subscribe({
    next: (data: number[]) => {
      if (this.pieChart) {
        this.pieChart.destroy();
      }

      // Extraemos los valores según el orden
      const trainingVisible = data[2];
      const nutritionVisible = data[3];
      const trainingHidden = data[5];
      const nutritionHidden = data[6];

      const total = data[0];
      const reported = data[1];
      const unreported = data[4];

      this.pieChart = new Chart(this.chartCanvas.nativeElement, {
        type: 'pie',
        data: {
          labels: [
            'Reportados',
            'No Reportados'
          ],
          datasets: [
            {
              data: [
                reported,
                unreported
              ],
              backgroundColor: ['#D96C6C', '#42A5F5'],
            }
          ]
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'top'
            },
            tooltip: {
              callbacks: {
                label: function (tooltipItem) {
                  const label = tooltipItem.label || '';
                  const value = tooltipItem.raw;
                  return `${label}: ${value}`;
                }
              }
            }
          }
        }
      });
    },
    error: err => {
      console.error('Error fetching reported comments data:', err);
    }
  });
}


}
