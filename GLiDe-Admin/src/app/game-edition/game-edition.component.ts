import { Component } from '@angular/core';
import {MatCardModule} from '@angular/material/card';
import {MatStepperModule} from '@angular/material/stepper';
import {MatFormField, MatHint, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MAT_DATE_LOCALE, MatOption, provideNativeDateAdapter} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {NgForOf, NgIf} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {GamificationEngineService} from '../services/gamification-engine.service';
import {TeacherService} from '../services/teachers.service';
import {
  MatDatepickerToggle,
  MatDateRangeInput,
  MatDateRangePicker,
  MatEndDate,
  MatStartDate
} from '@angular/material/datepicker';
import {MatButton, MatMiniFabButton} from '@angular/material/button';
import {MatListModule} from '@angular/material/list';
import {MatIcon} from '@angular/material/icon';
import {LeaderboardEditionComponent} from '../leaderboard-edition/leaderboard-edition.component';
import {RuleEditionComponent} from '../rule-edition/rule-edition.component';
import {TeacherEditionComponent} from '../teacher-edition/teacher-edition.component';
import {catchError, of, switchMap} from 'rxjs';
import {DateFormatService} from '../services/date-format.service';

@Component({
  selector: 'app-game-edition',
  imports: [MatCardModule, MatStepperModule, MatFormField, MatLabel, MatOption, MatSelect, NgForOf, ReactiveFormsModule, MatDateRangeInput, MatDateRangePicker, MatDatepickerToggle, MatEndDate, MatHint, MatStartDate, MatSuffix, MatButton, MatListModule, NgIf, MatIcon, MatMiniFabButton, LeaderboardEditionComponent, RuleEditionComponent, TeacherEditionComponent],
  providers: [provideNativeDateAdapter(), [{provide: MAT_DATE_LOCALE, useValue: 'en-GB'}]],
  templateUrl: './game-edition.component.html',
  styleUrl: './game-edition.component.css'
})
export class GameEditionComponent {
  game: any;
  games: any;
  simpleRules: any;
  dateRules: any;
  leaderboards: any;
  achievements: any;
  teachers: any;
  selectedFile: any;
  evaluableActions: any;

  constructor(private service: GamificationEngineService, private dateService: DateFormatService, private teachersService: TeacherService) {}

  gameForm: FormGroup = new FormGroup({
    game: new FormControl,
  });

  dateForm: FormGroup = new FormGroup({
    startDate: new FormControl,
    endDate: new FormControl,
  })

  ngOnInit(){
    this.service.getGames().subscribe((result) => {
      this.games = result;
    });
    this.service.getAchievements().subscribe((result) => {
      this.achievements = result;
    });
    this.service.getEvaluableActions().subscribe((result) => {
      this.evaluableActions = result;
    })
  }

  onGameSelect(){
    this.game = this.gameForm.get('game')?.value;
    this.dateForm.get('startDate')?.setValue(this.game.startDate);
    this.dateForm.get('endDate')?.setValue(this.game.endDate);
    this.service.getSimpleRules(this.game.subjectAcronym, this.game.course, this.game.period).subscribe((result) => {
      this.simpleRules = result;
    });
    this.service.getDateRules(this.game.subjectAcronym, this.game.course, this.game.period).subscribe((result) => {
      this.dateRules = result;
    });
    this.service.getLeaderboards(this.game.subjectAcronym, this.game.course, this.game.period).subscribe((result) => {
      this.leaderboards = result;
    });

    this.teachersService.getTeachersForGame(this.game.subjectAcronym, this.game.course, this.game.period).subscribe((result) => {
      this.teachers = result;
    });
  }

  updateGame(){
    let startDate = this.dateForm.get('startDate')?.value;
    let endDate = this.dateForm.get('endDate')?.value;

    startDate = this.dateService.formatDate(startDate);
    endDate = this.dateService.formatDate(endDate);

    this.service.updateGame(this.game.subjectAcronym, this.game.course, this.game.period, startDate, endDate).subscribe({
      next: (result) => {
        alert('Game updated successfully.');
        this.game = result;
        this.gameForm.get('game')?.setValue(result);
        for (let game in this.games){
          if (this.games[game].subjectAcronym === this.game.subjectAcronym && this.games[game].course === this.game.course && this.games[game].period === this.game.period) {
            this.games[game] = this.game;
            break;
          }
        }
      },
      error: () => alert('An unexpected error occurred.')
    })
  }

  onFileSelected(event: Event){
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0){
      this.selectedFile = input.files![0];
    }
  }

  deleteFile(inputElement: HTMLInputElement){
    this.selectedFile = null;
    inputElement.value='';
  }

  submitCSV(inputElement: HTMLInputElement){
    let game = this.gameForm.get('game')?.value
    this.service.postGameGroup(
      game.subjectAcronym, game.course, game.period, 10
    ).pipe(
      catchError(() => {
        console.log("Game group exists")
        return of(null);
      }),
      switchMap(() => {
        return this.service.postImportData(
          game.subjectAcronym, game.course, game.period, this.selectedFile
        )
      })
    ).subscribe({
      next: () => {
        alert('User and team data entered successfully.');
        this.deleteFile(inputElement);
      },
      error: () => alert('An unexpected error occurred.')
    });
  }
}
