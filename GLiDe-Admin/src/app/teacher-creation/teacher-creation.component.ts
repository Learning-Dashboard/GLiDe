import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { NgForOf, NgIf } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MAT_DATE_LOCALE, provideNativeDateAdapter } from '@angular/material/core';
import { forkJoin } from 'rxjs';
import { GamificationEngineService } from '../services/gamification-engine.service';
import { TeacherService, Teacher } from '../services/teachers.service';

@Component({
  selector: 'app-teacher-creation',
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    NgForOf,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    NgIf
  ],
  providers: [provideNativeDateAdapter(), [{ provide: MAT_DATE_LOCALE, useValue: 'en-GB' }]],
  templateUrl: './teacher-creation.component.html',
  standalone: true,
  styleUrl: './teacher-creation.component.css'
})
export class TeacherCreationComponent {
  games: any[] = [];
  loading = false;
  error: string | null = null;
  successMessage: string | null = null;
  constructor(
    private teacherService: TeacherService,
    private gamificationService: GamificationEngineService,
    private router: Router
  ) {}


  form: FormGroup = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    name: new FormControl('', [Validators.required, Validators.minLength(2)]),
    surname: new FormControl('', [Validators.required, Validators.minLength(2)]),
    games: new FormControl([]) // No validators, selecting a game is optional
  });

  ngOnInit() {
    // Load all available games for assignment
    this.gamificationService.getGames().subscribe({
      next: (games: any) => {
        // Filter out finished games, similar to leaderboard creation
        const activeGames = [];
        for (let gameKey in games) {
          if (games[gameKey].state !== 'Finished') {
            activeGames.push(games[gameKey]);
          }
        }
        this.games = activeGames;
      },      error: (error: any) => {
        console.error('Error loading games:', error);
        this.error = 'Error loading games. Please try again.';
      }
    });
  }
  onSubmit(): void {
    if (this.form.valid) {
      this.loading = true;
      this.error = null;
      this.successMessage = null;

      const teacherData: Teacher = {
        email: this.form.value.email,
        name: this.form.value.name,
        surname: this.form.value.surname
      };

      const selectedGames = this.form.value.games || [];

      // First create the teacher
      this.teacherService.createTeacher(teacherData).subscribe({
        next: () => {
          // Always assign games since they are required
          this.assignGamesToTeacher(teacherData.email, selectedGames);
          alert('Teacher created successfully!');
        },
        error: (error: any) => {
          this.error = 'Error creating teacher: ' + (error.message || error);
          alert(this.error); 
          this.loading = false;
        }
      });
    }
  }
  private assignGamesToTeacher(teacherEmail: string, games: any[]): void {
    // Create an array of observables for game assignments
    const gameAssignments = games.map(game => 
      this.teacherService.assignGameToTeacher(
        teacherEmail,
        game.subjectAcronym,
        game.course,
        game.period
      )
    );

    // Execute all game assignments in parallel
    forkJoin(gameAssignments).subscribe({
      next: () => {
        this.successMessage = 'Teacher created successfully';
        alert(this.successMessage); // Show success message using alert
        this.form.reset();
        this.form.get('games')?.setValue([]); 
      },
      error: (error) => {
        const errorMessage = error?.error?.message || 'Failed to create teacher';
        alert(errorMessage); 
      },
      complete: () => {
        this.loading = false;
        this.router.navigate(['/teachers']);
      },
    });
  }
}