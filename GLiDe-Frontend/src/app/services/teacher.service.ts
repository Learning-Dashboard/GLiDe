import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

interface TeacherGame {
  id: number;
  gameSubjectAcronym: string;
  gameCourse: number;
  gamePeriod: string;
  teacherEmail: string;
}

interface Student {
  playername: string;
  teamPlayername: string;
  level: number;
  points: number;
}

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getTeacherProfile(idToken: string): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${idToken}`);
    return this.http.get(`${this.baseUrl}/teachers/profile`, { headers });
  }

  getTeacherGames(idToken: string): Observable<TeacherGame[]> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${idToken}`);
    return this.http.get<TeacherGame[]>(`${this.baseUrl}/teachers/games`, { headers });
  }

  getGameStudents(subjectAcronym: string, course: number, period: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/students/game/${subjectAcronym}/${course}/${period}`);
  }
}
