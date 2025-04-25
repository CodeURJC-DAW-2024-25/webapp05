import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { TrainingListComponent } from './components/training/training-list/training-list.component';
import { TrainingDetailComponent } from './components/training/training-detail/training-detail.component';
import { TrainingFormComponent } from './components/training/training-form/training-form.component';
import { NutritionListComponent } from './components/nutrition/nutrition-list/nutrition-list.component';
import { NutritionDetailComponent } from './components/nutrition/nutrition-detail/nutrition-detail.component';
import { NutritionFormComponent } from './components/nutrition/nutrition-form/nutrition-form.component';
import {HomeComponent} from "./components/viewsComponent/home/home.component";
import { TrainingCommentListComponent } from './components/trainingComment/training-comment-list/training-comment-list.component';
import { NutritionCommentFormComponent } from './components/nutritionComment/nutritionComment-form/nutritionComment-form.component';
import { NutritionCommentListComponent } from './components/nutritionComment/nutritionComment-list/nutritionComment-list.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'training', component: TrainingListComponent },
  { path: 'training/:id', component: TrainingDetailComponent },
  { path: 'training/edit/:id', component: TrainingFormComponent },
  { path: 'training/new/:id', component: TrainingFormComponent },
  { path: 'nutrition', component: NutritionListComponent },
  { path: 'nutrition/:id', component: NutritionDetailComponent },
  { path: 'nutrition/edit/:id', component: NutritionFormComponent },
  { path: 'nutrition/new/:id', component: NutritionFormComponent },
  { path: 'trainingComments/:id', component: TrainingCommentListComponent },
  { path: 'nutritionComments/:id', component: NutritionCommentListComponent },
  { path: 'nutritionComments/new/:id', component: NutritionCommentFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
