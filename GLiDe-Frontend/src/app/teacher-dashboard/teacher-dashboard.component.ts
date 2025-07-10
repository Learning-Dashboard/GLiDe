import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { TeacherService } from '../services/teacher.service';
import { LearningdashboardService } from '../services/learningdashboard.service';
import { catchError, forkJoin, of, switchMap } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-teacher-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatIconModule
  ],
  templateUrl: './teacher-dashboard.component.html',
  styleUrls: ['./teacher-dashboard.component.css']
})
export class TeacherDashboardComponent implements OnInit {
  teacherProfile: any = null;
  gamesWithStudents: any[] = [];
  loading = true;
  error = '';

  constructor(
    private teacherService: TeacherService,
    private learningdashboardService: LearningdashboardService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadTeacherData();
  }

  loadTeacherData(): void {
    const idToken = localStorage.getItem('idToken');

    if (!idToken) {
      this.router.navigate(['/login']);
      return;
    }

    this.teacherService.getTeacherProfile(idToken).pipe(
      switchMap((profile: any) => {
        this.teacherProfile = profile;
        return this.teacherService.getTeacherGames(idToken);
      }),
      catchError((error: any) => {
        console.error('Error loading teacher profile or games:', error);
        this.error = 'Error al cargar el perfil o los games del profesor';
        this.loading = false;
        return of([]);
      })
    ).subscribe((games: any[]) => {
      this.loadStudentsForGames(games);
    });
  }

  loadStudentsForGames(games: any[]): void {
    const gameObservables = games.map(game =>
      this.teacherService.getGameStudents(game.gameSubjectAcronym, game.gameCourse, game.gamePeriod).pipe(
        catchError((error: any) => {
          console.error(`Error loading students for game ${game.gameSubjectAcronym}:`, error);
          return of([]); // Si falla, devolvemos una lista vacía
        })
      )
    );

    forkJoin(gameObservables).subscribe((results: any[]) => {
      // Construimos la estructura con los flags showTeams y showStudents
      this.gamesWithStudents = results.map((students, index) => {
        const teams = this.groupStudentsByTeam(students || []).map(team => ({
          ...team,
          showStudents: false // <- este controla el despliegue de estudiantes
        }));

        return {
          game: games[index],
          teams,
          showTeams: false // <- este controla el despliegue de los equipos
        };
      });

      this.loading = false;
    });
  }


  groupStudentsByTeam(students: any[]): any[] {
    const teamsMap = new Map<string, any[]>();

    students.forEach(student => {
      if (!teamsMap.has(student.teamPlayername)) {
        teamsMap.set(student.teamPlayername, []);
      }
      teamsMap.get(student.teamPlayername)!.push(student);
    });

    return Array.from(teamsMap.entries()).map(([teamName, students]) => ({
      teamName,
      students
    }));
  }
  
  viewStudentDetails(studentName: string, game: any): void {
    
    localStorage.setItem('gameSubjectAcronym', game.gameSubjectAcronym);
    localStorage.setItem('gameCourse', game.gameCourse);
    localStorage.setItem('gamePeriod', game.gamePeriod);
    
    this.router.navigate(['/teacher-player-metrics', studentName]);
  }

  getGameDisplayName(game: any): string {
    return `${game.gameSubjectAcronym} - ${game.gameCourse} - ${game.gamePeriod}`;
  }

  getTotalStudents(teams: any[]): number {
    return teams.reduce((total, team) => total + team.students.length, 0);
  }

  onHover(event: Event) {
    (event.target as HTMLElement).style.backgroundColor = '#f5f5f5';
  }

  onLeave(event: Event) {
    (event.target as HTMLElement).style.backgroundColor = 'transparent';
 }


  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}