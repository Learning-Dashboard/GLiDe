import { Component } from '@angular/core';
import {GamificationEngineService} from '../services/gamification-engine.service';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectChange, MatSelectModule} from '@angular/material/select';
import {NgForOf, NgIf} from '@angular/common';
import {MatInputModule} from '@angular/material/input';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import {MAT_DATE_LOCALE, provideNativeDateAdapter} from '@angular/material/core';
import {forkJoin, of, switchMap} from 'rxjs';
import {DateFormatService} from '../services/date-format.service';

@Component({
  selector: 'app-leaderboard-creation',
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    NgForOf,
    MatInputModule,
    MatDatepickerModule,
    MatCheckboxModule,
    MatButtonModule,
    NgIf
  ],
  providers: [provideNativeDateAdapter(), [{provide: MAT_DATE_LOCALE, useValue: 'en-GB'}]],
  templateUrl: './leaderboard-creation.component.html',
  standalone: true,
  styleUrl: './leaderboard-creation.component.css'
})
export class LeaderboardCreationComponent {
  games: any;
  selectedGame: any;
  achievements: any;
  filteredAchievements: any = [];
  constructor(private service: GamificationEngineService, private dateService: DateFormatService) {}

  form: FormGroup = new FormGroup({
    game: new FormControl,
    achievement: new FormControl,
    name: new FormControl,
    startDate: new FormControl,
    endDate: new FormControl,
    assessmentLevel: new FormControl,
    anonymization: new FormControl,
    studentVisible: new FormControl(false)
  });

  ngOnInit(){
    let selectedGame = localStorage.getItem('selectedGame');
    if(selectedGame) selectedGame = JSON.parse(selectedGame);
    forkJoin({
      gamesResult: this.service.getGames(),
      achievementsResult: this.service.getAchievements(),
    }).pipe(
      switchMap(({ gamesResult, achievementsResult }) => {
        let games: any = gamesResult;
        let finalGames = [];
        for (let game in games){
          if(games[game].state !== 'Finished'){
            finalGames.push(games[game]);
            if(JSON.stringify(selectedGame) === JSON.stringify(games[game])) this.form.get('game')?.setValue(games[game]);
          }
        }
        if(finalGames.length !== 0) this.games = finalGames;
        this.achievements = achievementsResult;

        this.selectedGame = this.form.get('game')?.value;
        if(this.selectedGame)
          return this.getGameRules(this.selectedGame);
        else
          return of(null);
      })
    ).subscribe((rules) => {
      if(rules){
        this.filterAchievements(rules.simpleRules, rules.dateRules);
        console.log(this.filteredAchievements);
      }
    });

  }

  onGameSelect(event: MatSelectChange){
    this.selectedGame = event.value;
    localStorage.setItem('selectedGame', JSON.stringify(this.selectedGame));
    this.getGameRules(this.selectedGame).subscribe(({simpleRules, dateRules}) => {
      this.filterAchievements(simpleRules, dateRules);
      console.log(this.filteredAchievements);
    })
  }

  getGameRules(game: any){
    return forkJoin({
      simpleRules: this.service.getSimpleRules(game.subjectAcronym, game.course, game.period),
      dateRules: this.service.getSimpleRules(game.subjectAcronym, game.course, game.period)
    });
  }

  filterAchievements(simpleRules: any, dateRules: any){
    this.filteredAchievements = [];
    let simpleRuleAchievements = simpleRules.map((rule: { achievementId: any; }) => rule.achievementId);
    let dateRuleAchievements = dateRules.map((rule: { achievementId: any; }) => rule.achievementId);
    let achievementIds = [...new Set(simpleRuleAchievements.concat(dateRuleAchievements))];
    for (let i in achievementIds) {
      for (let j in this.achievements) {
        if (achievementIds[i] === this.achievements[j].id) this.filteredAchievements.push(this.achievements[j]);
      }
    }
  }

  resetForm(){
    this.form.reset();
    this.form.get('studentVisible')?.setValue(false);
  }

  onSubmit(){
    let game = this.form.get('game')?.value;
    let startDate = this.form.get('startDate')?.value;
    let endDate = this.form.get('endDate')?.value;

    startDate = this.dateService.formatDate(startDate);
    endDate = this.dateService.formatDate(endDate);

    this.service.postLeaderboard(
      this.form.get('name')?.value,
      startDate,
      endDate,
      this.form.get('assessmentLevel')?.value,
      'Subject',
      this.form.get('anonymization')?.value,
      this.form.get('studentVisible')?.value,
      game.subjectAcronym,
      game.course,
      game.period,
      this.form.get('achievement')?.value
    ).subscribe({
      next: (result) => {
        console.log(result.status);
        alert('Leaderboard created successfully.');
        this.resetForm();
      },
      error: () => alert('An unexpected error occurred.')
    });
  }
}
