import { UserService } from '../../services/user.service';
import { LoginService } from '../../services/login.service';
import { Component, OnInit, ViewChild,ElementRef, AfterViewInit,} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import Chart from 'chart.js/auto';
import { UserDTO } from '../../dto/user.dto';
import { TrainingCommentDTO } from '../../dto/training-comment.dto';
import { NutritionCommentDTO } from '../../dto/nutrition-comment.dto';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
})
export class AdminComponent implements OnInit, AfterViewInit {
  @ViewChild('chartCanvas') chartCanvas!: ElementRef;
  pieChart: any;

  admin: UserDTO | null = null;
  reportedTrainingComments: TrainingCommentDTO[] = [];
  reportedNutritionComments: NutritionCommentDTO[] = [];
  shouldGenerateChart = false;

  constructor(
    private loginService: LoginService,
    private userService: UserService,
    private toastr: ToastrService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginService.currentUser.subscribe((user) => {
      this.admin = user;
      if (user) {
      this.shouldGenerateChart = true;
      } else {
        this.admin = null;
        this.shouldGenerateChart = false;
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.shouldGenerateChart) {
      this.generateChart();
    }
  }

  loadReportedComments(): void {
    this.userService.loadReportedNutritionComments().subscribe(comments => {
      this.reportedNutritionComments = comments;
    });

    this.userService.loadReportedTrainingComments().subscribe(comments => {
      this.reportedTrainingComments = comments;
    });
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
            labels: ['Reportados', 'No Reportados'],
            datasets: [
              {
                data: [reported, unreported],
                backgroundColor: ['#D96C6C', '#42A5F5'],
              },
            ],
          },
          options: {
            responsive: true,
            plugins: {
              legend: {
                position: 'top',
              },
              tooltip: {
                callbacks: {
                  label: function (tooltipItem) {
                    const label = tooltipItem.label || '';
                    const value = tooltipItem.raw;
                    return `${label}: ${value}`;
                  },
                },
              },
            },
          },
        });
      },
      error: (err) => {
        console.error('Error fetching reported comments data:', err);
      },
    });
  }
}
