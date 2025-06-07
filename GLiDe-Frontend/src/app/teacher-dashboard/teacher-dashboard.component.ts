import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { TeacherService } from '../services/teacher.service';
import { LearningdashboardService } from '../services/learningdashboard.service';

@Component({
  selector: 'app-teacher-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './teacher-dashboard.component.html',
  styleUrls: ['./teacher-dashboard.component.css']
})
export class TeacherDashboardComponent {
  teacherProfile: any = null;
  gamesWithStudents: any[] = [];
  loading = true;
  error = '';

  constructor(
    private teacherService: TeacherService,
    private learningdashboardService: LearningdashboardService,
    private router: Router  ) {}

  ngOnInit(): void {
    this.loadTeacherData();
  }

  loadTeacherData(): void {
    const idToken = localStorage.getItem('idToken');
    
    if (!idToken) {
      this.router.navigate(['/login']);
      return;
    }

    // Cargar perfil del profesor
    this.teacherService.getTeacherProfile(idToken).subscribe((profile: any) => {
      this.teacherProfile = profile;
      this.loadTeacherGames(idToken);
    }, (error: any) => {      console.error('Error loading teacher profile:', error);
      this.error = 'Error al cargar el perfil del profesor';
      this.loading = false;
    });
  }

  loadTeacherGames(idToken: string): void {
    this.teacherService.getTeacherGames(idToken).subscribe((games: any[]) => {
      this.loadStudentsForGames(games);
    }, (error: any) => {      console.error('Error loading teacher games:', error);
      this.error = 'Error al cargar los games del profesor';
      this.loading = false;
    });
  }

  loadStudentsForGames(games: any[]): void {
    const gamePromises = games.map(game => 
      this.teacherService.getGameStudents(game.gameSubjectAcronym, game.gameCourse, game.gamePeriod)
        .toPromise()
        .then((students: any[] | undefined) => {
          const teams = this.groupStudentsByTeam(students || []);
          return { game, teams };
        })        .catch((error: any) => {
          console.error(`Error loading students for game ${game.gameSubjectAcronym}:`, error);
          return { game, teams: [] };
        })
    );    Promise.all(gamePromises).then((gamesWithStudents: any) => {
      this.gamesWithStudents = gamesWithStudents;
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
    }));  }

  viewStudentDetails(studentName: string): void {
    this.router.navigate(['/teacher-player-metrics', studentName]);
  }

  getGameDisplayName(game: any): string {
    return `${game.gameSubjectAcronym} - Curso ${game.gameCourse} - ${game.gamePeriod}`;
  }

  getTotalStudents(teams: any[]): number {
    return teams.reduce((total, team) => total + team.students.length, 0);
  }

  logout(): void {
    localStorage.removeItem('idToken');
    localStorage.removeItem('loggedUser');
    localStorage.removeItem('userType');
    this.router.navigate(['/login']);
  }
}
