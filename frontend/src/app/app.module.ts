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
import { HomeComponent } from './components/viewsComponent/home/home.component';
import { NavbarComponent } from './components/viewsComponent/navbar/navbar.component';
import { HttpClientModule } from '@angular/common/http';
import {ToastrModule} from "ngx-toastr";
import { provideHttpClient, withFetch } from '@angular/common/http';


@NgModule({
  declarations: [
    AppComponent,
    TrainingListComponent,
    TrainingDetailComponent,
    TrainingFormComponent,
    HomeComponent,
    NavbarComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule, // Requerido por ngx-toastr
    ToastrModule.forRoot(),
  ],
  providers: [provideHttpClient(withFetch())],
  bootstrap: [AppComponent]
})
export class AppModule { }
