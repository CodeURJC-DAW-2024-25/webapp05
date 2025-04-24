import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { TrainingListComponent } from './components/training/training-list/training-list.component';
import { TrainingDetailComponent } from './components/training/training-detail/training-detail.component';
import { TrainingFormComponent } from './components/training/training-form/training-form.component';
import {HomeComponent} from "./components/viewsComponent/home/home.component";
import { TrainingCommentListComponent } from './components/trainingComment/training-comment-list/training-comment-list.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'training', component: TrainingListComponent },
  { path: 'training/:id', component: TrainingDetailComponent },
  { path: 'training/edit/:id', component: TrainingFormComponent },
  { path: 'training/new/:id', component: TrainingFormComponent },
  { path: 'trainingComments/:id', component: TrainingCommentListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
