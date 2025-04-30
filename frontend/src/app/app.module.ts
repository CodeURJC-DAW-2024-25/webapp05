import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';

import { TrainingListComponent } from './components/training/training-list/training-list.component';
import { TrainingDetailComponent } from './components/training/training-detail/training-detail.component';
import { TrainingFormComponent } from './components/training/training-form/training-form.component';
import { NutritionListComponent } from './components/nutrition/nutrition-list/nutrition-list.component';
import { NutritionDetailComponent } from './components/nutrition/nutrition-detail/nutrition-detail.component';
import { NutritionFormComponent } from './components/nutrition/nutrition-form/nutrition-form.component';
import { HomeComponent } from './components/viewsComponent/home/home.component';
import { NavbarComponent } from './components/viewsComponent/navbar/navbar.component';
import { FooterComponent } from './components/viewsComponent/footer/footer.component';
import {ToastrModule} from "ngx-toastr";
import { provideHttpClient, withFetch } from '@angular/common/http';
import { TrainingCommentListComponent } from './components/trainingComment/training-comment-list/training-comment-list.component';
import { TrainingCommentFormComponent } from './components/trainingComment/training-comment-form/training-comment-form.component';
import { NutritionCommentListComponent } from './components/nutritionComment/nutritionComment-list/nutritionComment-list.component';
import { NutritionCommentFormComponent } from './components/nutritionComment/nutritionComment-form/nutritionComment-form.component';
import { LoginComponent } from './components/viewsComponent/login/login.component';
import { RegisterComponent } from './components/viewsComponent/register/register.component';
import { AccountComponent } from './components/account/account.component';
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [
    AppComponent,
    TrainingListComponent,
    TrainingDetailComponent,
    TrainingFormComponent,
    NutritionListComponent,
    NutritionDetailComponent,
    NutritionFormComponent,
    HomeComponent,
    NavbarComponent,
    FooterComponent,
    TrainingCommentListComponent,
    TrainingCommentFormComponent,
    NutritionCommentListComponent,
    NutritionCommentFormComponent,
    LoginComponent,
    RegisterComponent,
    AccountComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    CommonModule,
  ],
  providers: [
    provideHttpClient(withFetch())  // Esta línea está bien como está si lo necesitas
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
