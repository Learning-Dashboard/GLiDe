import { Component } from '@angular/core';
import {GamificationEngineService} from '../services/gamification-engine.service';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectChange, MatSelectModule} from '@angular/material/select';
import {FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatRadioModule} from '@angular/material/radio';
import {NgForOf, NgIf} from '@angular/common';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MAT_DATE_LOCALE, provideNativeDateAdapter} from '@angular/material/core';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {DateFormatService} from '../services/date-format.service';

@Component({
  selector: 'app-rule-creation',
  imports: [MatFormFieldModule, MatInputModule, MatSelectModule, ReactiveFormsModule, MatCardModule, MatButtonModule, MatRadioModule, NgForOf, NgIf, MatDatepickerModule, MatCheckboxModule],
  providers: [provideNativeDateAdapter(), [{provide: MAT_DATE_LOCALE, useValue: 'en-GB'}]],
  templateUrl: './rule-creation.component.html',
  standalone: true,
  styleUrl: './rule-creation.component.css'
})
export class RuleCreationComponent {
  evaluableActions: any;
  games: any;
  achievements: any;
  canCallOpenAI: boolean = false;
  fetchingOpenAI: boolean = false;
  protected conditions = [
    ['ValueGreaterThan', '>'],
    ['ValueLessThan', '<'],
    ['ValueEqualTo', '='],
    ['ValueGreaterThanOrEqualTo', '>='],
    ['ValueLessThanOrEqualTo', '<='],
    ['ValueOutsideOfRange', 'out of range'],
    ['ValueInsideOfRange', 'inside range']
  ];

  constructor(private service: GamificationEngineService, private dateService: DateFormatService) {}

  form: FormGroup = new FormGroup({
    game: new FormControl,
    evaluableAction: new FormControl,
    achievement: new FormControl,
    ruleType: new FormControl('simple'),
    startDate: new FormControl,
    endDate: new FormControl,
    name: new FormControl,
    repetitions: new FormControl,
    achievementAssignmentMessage: new FormControl,
    onlyFirstTime: new FormControl(false),
    achievementAssignmentUnits: new FormControl,
    achievementAssignmentCondition: new FormControl,
    achievementAssignmentParameters: new FormArray([new FormControl()])
  });

  get achievementAssignmentParameters(){
    return this.form.get("achievementAssignmentParameters") as FormArray;
  }
/*
  addAchievementAssignmentParameter(){
    this.achievementAssignmentParameters.push(new FormControl('', Validators.required));
  }

  removeAchievementAssignmentParameter(i: number){
    this.achievementAssignmentParameters.removeAt(i);
  }

 */

  openAiParameterChange(){
    if (this.achievementAssignmentParameters.valid) this.canCallOpenAI = true;
  }

  protected conditionChange(){
    this.openAiParameterChange();
    if (this.form.get('achievementAssignmentCondition')?.value !== 'ValueInsideOfRange' && this.form.get('achievementAssignmentCondition')?.value !== 'ValueOutsideOfRange') {
      const firstValue = this.achievementAssignmentParameters.at(0);
      this.achievementAssignmentParameters.clear();
      this.achievementAssignmentParameters.push(firstValue);
    }
    else if (this.achievementAssignmentParameters.length < 2) {
      this.achievementAssignmentParameters.push(new FormControl('', Validators.required));
    }
  }

  resetForm(){
    this.form.reset();
    this.form.get('ruleType')?.setValue('simple');
    //this.conditionChange();
    this.canCallOpenAI = true;
    this.form.get('onlyFirstTime')?.setValue(false);
  }

  ngOnInit(){
    let selectedGame = localStorage.getItem('selectedGame');
    if(selectedGame) selectedGame = JSON.parse(selectedGame);
    this.service.getEvaluableActions().subscribe((result) => {
      this.evaluableActions = result;
    });
    this.service.getGames().subscribe((result) => {
      let games: any = result;
      let finalGames = [];
      for (let game in games){
        if(games[game].state !== 'Finished'){
          finalGames.push(games[game]);
          if(JSON.stringify(selectedGame) === JSON.stringify(games[game])) this.form.get('game')?.setValue(games[game]);
        }
      }
      if(finalGames.length !== 0) this.games = finalGames;
    });
    this.service.getAchievements().subscribe((result) => {
      this.achievements = result;
    });
  }

  getRecommendedNameMessage(){
    let evaluableAction = this.form.get('evaluableAction')?.value;
    this.canCallOpenAI = false;
    this.fetchingOpenAI = true;
    this.service.postOpenAI(evaluableAction.description,
      this.form.get('achievementAssignmentCondition')?.value,
      this.form.get('achievementAssignmentParameters')?.value).subscribe({
      next: (result) => {
        let response: any = result;
        this.form.get('name')?.setValue(response.name);
        this.form.get('achievementAssignmentMessage')?.setValue(response.achievementAssignmentMessage);
        this.fetchingOpenAI = false;
      },
      error: () => {
        alert('An error occurred while fetching recommended name and message.');
        this.fetchingOpenAI = false;
      }
    });
  }

  onGameSelect(event: MatSelectChange){
    let game = event.value;
    localStorage.setItem('selectedGame', JSON.stringify(game));
  }

  onSubmit(){
    console.log(this.form.get('game')?.value);
    console.log(this.form.get('evaluableAction')?.value);
    console.log(this.form.get('achievement')?.value);
    console.log(this.form.get('ruleType')?.value);
    console.log(this.form.get('startDate')?.value);
    console.log(this.form.get('endDate')?.value);
    console.log(this.form.get('name')?.value);
    console.log(this.form.get('repetitions')?.value);
    console.log(this.form.get('achievementAssignmentMessage')?.value);
    console.log(this.form.get('onlyFirstTime')?.value);
    console.log(this.form.get('achievementAssignmentUnits')?.value);
    console.log(this.form.get('achievementAssignmentCondition')?.value);
    console.log(this.form.get('achievementAssignmentParameters')?.value);

    let game = this.form.get('game')?.value;
    let evaluableAction = this.form.get('evaluableAction')?.value;
    let repetitions = Math.round(this.form.get('repetitions')?.value);
    let points = Math.round(this.form.get('achievementAssignmentUnits')?.value);
    if (this.form.get('ruleType')?.value === 'simple'){
      this.service.postSimpleRule(
        this.form.get('name')?.value,
        repetitions,
        game.subjectAcronym,
        game.course,
        game.period,
        evaluableAction.id,
        this.form.get('achievement')?.value,
        this.form.get('achievementAssignmentMessage')?.value,
        this.form.get('onlyFirstTime')?.value,
        this.form.get('achievementAssignmentCondition')?.value,
        this.form.get('achievementAssignmentParameters')?.value,
        points,
        evaluableAction.assessmentLevel
      ).subscribe({
        next: (result) => {
          console.log(result.status);
          alert('Simple rule created successfully.');
          this.resetForm();
        },
        error: () => alert('An unexpected error occurred.')
      });
    }
    else if (this.form.get('ruleType')?.value === 'date'){
      let startDate = this.form.get('startDate')?.value;
      let endDate = this.form.get('endDate')?.value;

      startDate = this.dateService.formatDate(startDate);
      endDate = this.dateService.formatDate(endDate);

      this.service.postDateRule(
        this.form.get('name')?.value,
        repetitions,
        game.subjectAcronym,
        game.course,
        game.period,
        evaluableAction.id,
        this.form.get('achievement')?.value,
        this.form.get('achievementAssignmentMessage')?.value,
        this.form.get('onlyFirstTime')?.value,
        this.form.get('achievementAssignmentCondition')?.value,
        this.form.get('achievementAssignmentParameters')?.value,
        points,
        evaluableAction.assessmentLevel,
        startDate,
        endDate
      ).subscribe({
        next: (result) => {
          console.log(result.status);
          alert('Date rule created successfully.');
          this.resetForm();
        },
        error: () => alert('An unexpected error occurred.')
      });
    }
  }
}
