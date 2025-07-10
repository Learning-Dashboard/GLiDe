import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { OnInit, Input } from '@angular/core';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {MatInputModule} from '@angular/material/input';

import {MAT_DATE_LOCALE, provideNativeDateAdapter} from '@angular/material/core';
import { TeacherService, Teacher, TeacherGame } from '../services/teachers.service';

@Component({
  selector: 'app-teacher-edition',
  imports: [RouterModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatListModule,
    MatCardModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    RouterModule],
  providers: [provideNativeDateAdapter(), [{provide: MAT_DATE_LOCALE, useValue: 'en-GB'}]],
  standalone: true,
  templateUrl: './teacher-edition.component.html',
  styleUrls: ['./teacher-edition.component.css']
})
export class TeacherEditionComponent implements OnInit {
  @Input() game: any; // Rebem el game seleccionat

  teachers: Teacher[] = [];
  assignedTeachers: TeacherGame[] = [];
  availableTeachers: Teacher[] = [];
  
  loading = false;
  error: string | null = null;

  newTeacherForm: FormGroup;
  showNewTeacherForm = false;

  constructor(
    private fb: FormBuilder,
    private teacherService: TeacherService
  ) {
    this.newTeacherForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      name: ['', [Validators.required, Validators.minLength(2)]],
      surname: ['', [Validators.required, Validators.minLength(2)]]
    });
  }

  ngOnInit(): void {
    if (this.game) {
      this.loadTeachers();
      this.loadAssignedTeachers();
    }
  }

  ngOnChanges(): void {
    // S'executa quan canvia el game seleccionat
    if (this.game) {
      this.loadTeachers();
      this.loadAssignedTeachers();
    }
  }

  loadTeachers(): void {
    this.loading = true;
    this.teacherService.getAllTeachers().subscribe({
      next: (teachers) => {
        this.teachers = teachers;
        this.updateAvailableTeachers();
        this.loading = false;
      },
      error: (error) => {
        alert('Error loading teachers: ' + error.message);
        this.loading = false;
      }
    });
  }

  loadAssignedTeachers(): void {
    if (!this.game) return;
    
    this.teacherService.getTeachersForGame(
      this.game.subjectAcronym,
      this.game.course,
      this.game.period
    ).subscribe({
      next: (assignedTeachers) => {
        this.assignedTeachers = assignedTeachers;
        this.updateAvailableTeachers();
      },
      error: (error) => {
        alert('Error loading assigned teachers: ' + error.message);
      }
    });
  }

  updateAvailableTeachers(): void {
    const assignedEmails = this.assignedTeachers.map(t => t.teacherEmail);
    this.availableTeachers = this.teachers.filter(t => !assignedEmails.includes(t.email));
  }

  assignTeacher(teacherEmail: string): void {
    if (!this.game) return;

    this.teacherService.assignGameToTeacher(
      teacherEmail,
      this.game.subjectAcronym,
      this.game.course,
      this.game.period
    ).subscribe({
      next: () => {
        this.loadAssignedTeachers();
      },
      error: (error) => {
        alert('Error assigning teacher: ' + error.message);
      }
    });
  }

  removeTeacher(teacherEmail: string): void {
    if (!this.game) return;

    if (confirm('Are you sure you want to remove this teacher from the game?')) {
      this.teacherService.removeGameFromTeacher(
        teacherEmail,
        this.game.subjectAcronym,
        this.game.course,
        this.game.period
      ).subscribe({
        next: () => {
          this.loadAssignedTeachers();
        },
        error: (error) => {
            alert('Error removing teacher: ' + error.message);
        }
      });
    }
  }

  deleteTeacher(email: string): void {
    if (confirm('Are you sure you want to delete this teacher? This will remove them from all games.')) {
      this.teacherService.deleteTeacher(email).subscribe({
        next: () => {
          this.loadTeachers();
          this.loadAssignedTeachers();
        },
        error: (error) => {
            alert('Error deleting teacher: ' + error.message);
        }
      });
    }
  }

  getTeacherName(email: string): string {
    const teacher = this.teachers.find(t => t.email === email);
    return teacher ? `${teacher.name} ${teacher.surname}` : email;
  }
}