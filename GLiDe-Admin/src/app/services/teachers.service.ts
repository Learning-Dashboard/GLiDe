import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Teacher {
  email: string;
  name: string;
  surname: string;
}

export interface TeacherGame {
  id?: number;
  teacherEmail: string;
  gameSubjectAcronym: string;
  gameCourse: number;
  gamePeriod: string;
}

@Injectable({
  providedIn: 'root'
})
export class TeacherService {
  private apiUrl = 'http://localhost:8080/api/teachers';

  constructor(private http: HttpClient) {}

  
  getTeacher(email: string): Observable<Teacher> {
    return this.http.get<Teacher>(`${this.apiUrl}/${email}`);
  }

  getAllTeachers(): Observable<Teacher[]> {
    return this.http.get<Teacher[]>(this.apiUrl);
  }

  createTeacher(teacher: Teacher): Observable<Teacher> {
    return this.http.post<Teacher>(this.apiUrl, teacher);
  }

  updateTeacher(email: string, teacher: Teacher): Observable<Teacher> {
    return this.http.put<Teacher>(`${this.apiUrl}/${email}`, teacher);
  }

  deleteTeacher(email: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${email}`);
  }

  getTeacherGames(teacherEmail: string): Observable<TeacherGame[]> {
    return this.http.get<TeacherGame[]>(`${this.apiUrl}/${teacherEmail}/games`);
  }

  assignGameToTeacher(teacherEmail: string, gameSubjectAcronym: string, gameCourse: number, gamePeriod: string): Observable<TeacherGame> {
    const params = { gameSubjectAcronym, gameCourse: gameCourse.toString(), gamePeriod };
    return this.http.post<TeacherGame>(`${this.apiUrl}/${teacherEmail}/games`, null, { params });
  }

  removeGameFromTeacher(teacherEmail: string, gameSubjectAcronym: string, gameCourse: number, gamePeriod: string): Observable<void> {
    const params = { gameSubjectAcronym, gameCourse: gameCourse.toString(), gamePeriod };
    return this.http.delete<void>(`${this.apiUrl}/${teacherEmail}/games`, { params });
  }

  getTeachersForGame(gameSubjectAcronym: string, gameCourse: number, gamePeriod: string): Observable<TeacherGame[]> {
    const params = { gameSubjectAcronym, gameCourse: gameCourse.toString(), gamePeriod };
    return this.http.get<TeacherGame[]>(`${this.apiUrl}/games/search`, { params });
  }
}