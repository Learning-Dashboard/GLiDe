import {Component, ViewChild} from '@angular/core';
import {MatStepper, MatStepperModule} from '@angular/material/stepper';
import {GamificationEngineService} from '../services/gamification-engine.service';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatRadioModule} from '@angular/material/radio';
import {MatSelectChange, MatSelectModule} from '@angular/material/select';
import {MatInput} from '@angular/material/input';
import {NgForOf, NgIf} from '@angular/common';
import {
  MatDatepickerToggle,
  MatDateRangeInput,
  MatDateRangePicker,
  MatEndDate,
  MatStartDate
} from '@angular/material/datepicker';
import {MAT_DATE_LOCALE, provideNativeDateAdapter} from '@angular/material/core';
import {MatListModule} from '@angular/material/list';
import {MatIcon} from '@angular/material/icon';
import {catchError, forkJoin, iif, of, switchMap, throwError} from 'rxjs';
import {Chart, registerables} from 'chart.js';
import {DateFormatService} from '../services/date-format.service';


@Component({
  selector: 'app-game-creation',
  imports: [MatStepperModule, MatCardModule, MatButtonModule, MatRadioModule, ReactiveFormsModule, FormsModule, MatSelectModule, MatInput, NgIf, NgForOf, MatDateRangeInput, MatDateRangePicker, MatDatepickerToggle, MatEndDate, MatStartDate, MatListModule, MatIcon],
  providers: [provideNativeDateAdapter(), [{provide: MAT_DATE_LOCALE, useValue: 'en-GB'}]],
  templateUrl: './game-creation.component.html',
  standalone: true,
  styleUrl: './game-creation.component.css'
})
export class GameCreationComponent {
  @ViewChild('stepper') stepper!: MatStepper;

  subjectType: string = 'existing';
  existingSubjects: any;
  games: any;
  simpleRules: any;
  dateRules: any;
  importedSimpleRules: any[] = [];
  importedDateRules: any[] = [];
  selectedSimpleRule: any;
  selectedDateRule: any;
  importChangeDate = false;
  importChangeSimple = false;
  selectedFile: any;
  chart: any;

  constructor(private service: GamificationEngineService, private dateService: DateFormatService) {
    Chart.register(...registerables);
  }

  subjectForm: FormGroup = new FormGroup({
    acronym: new FormControl,
    name: new FormControl,
    code: new FormControl,
    studies: new FormControl,
    school: new FormControl
  });
  existingSubjectForm: FormGroup = new FormGroup({
    subject: new FormControl
  });
  gameForm: FormGroup = new FormGroup({
    subject_acronym: new FormControl,
    course: new FormControl,
    period: new FormControl,
    start_date: new FormControl,
    end_date: new FormControl,
    valid: new FormControl(true)
  });
  gameLevelPolicyForm: FormGroup = new FormGroup({
    a: new FormControl(1),
    b: new FormControl(1.4),
    c: new FormControl(2)
  });

  onSelectionChange(event: any){
    if(event.previouslySelectedIndex === 0){
      if (this.subjectType === 'existing') this.gameForm.controls['subject_acronym']
        .setValue((this.existingSubjectForm.get('subject')?.value).acronym);
      else this.gameForm.controls['subject_acronym']
        .setValue(this.subjectForm.get('acronym')?.value);
    }
  }

  onGameSelect(event: MatSelectChange){
    let game = event.value;
    this.service.getSimpleRules(game.subjectAcronym, game.course, game.period).subscribe((result) => {
      this.simpleRules = result;
    });
    this.service.getDateRules(game.subjectAcronym, game.course, game.period).subscribe((result) => {
      this.dateRules = result;
    });
  }

  selectSimpleRule(rule: any){
    if (!this.importChangeSimple) {
      this.selectedSimpleRule = rule;
    } else {
      this.importChangeSimple = false;
    }
  }

  importAllSimpleRules(){
    this.importedSimpleRules = this.importedSimpleRules.concat(this.simpleRules);
  }

  importSimpleRule(index: number){
    this.importedSimpleRules.push(this.simpleRules[index]);
    this.importChangeSimple = true;
  }

  removeImportedSimpleRule(index: number){
    this.importedSimpleRules.splice(index, 1);
    this.importChangeSimple = true;
  }

  selectDateRule(rule: any){
    if (!this.importChangeDate){
      this.selectedDateRule = rule;
    } else {
      this.importChangeDate = false;
    }
  }

  importAllDateRules(){
    this.importedDateRules = this.importedDateRules.concat(this.dateRules);
    this.noInvalidDateRange();
  }

  importDateRule(index: number){
    this.importedDateRules.push(this.dateRules[index]);
    this.importChangeDate = true;
    this.noInvalidDateRange();
  }

  removeImportedDateRule(index: number){
    this.importedDateRules.splice(index, 1);
    this.selectedDateRule = null;
    this.importChangeDate = true;
    this.noInvalidDateRange();
  }

  invalidDates(dateRule: any){
    let gameStartDate = this.gameForm.get('start_date')?.value;
    let gameEndDate = this.gameForm.get('end_date')?.value;
    if (!gameEndDate) return true;
    gameStartDate = new Date(gameStartDate);
    gameEndDate = new Date(gameEndDate);
    let startDate = new Date(dateRule.startDate);
    let endDate = new Date(dateRule.endDate);
    return (startDate < gameStartDate || startDate > gameEndDate || endDate < gameStartDate || endDate > gameEndDate);
  }

  noInvalidDateRange(){
    let invalid = false;
    for (let dateRule in this.importedDateRules){
      if (this.invalidDates(this.importedDateRules[dateRule])) {
        this.gameForm.controls['valid'].setValue(null);
        invalid = true;
        break;
      }
    }
    if (!invalid) this.gameForm.controls['valid'].setValue(true);
  }

  formatDate(dateString: string){
    let dateArray = dateString.split('/')
    return dateArray[2] + '-' + dateArray[1] + '-' + dateArray[0];
  }

  changeDateRuleDates(start: any, end: any){
    if (start.value && end.value){
      let startDate = this.formatDate(start.value);
      let endDate = this.formatDate(end.value);
      this.selectedDateRule.startDate = startDate;
      this.selectedDateRule.endDate = endDate;
      this.noInvalidDateRange();
    }
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

  ngOnInit(){
    this.service.getGames().subscribe((result) => {
      this.games = result;
    });
    this.service.getSubjects().subscribe((result) => {
      this.existingSubjects = result;
    })
  }

  ngAfterViewInit(): void {
    this.initializeChart();
    this.gameLevelPolicyForm.valueChanges.subscribe(() => {
      this.updateChart();
    });
  }

  initializeChart(){
    const ctx = document.getElementById('levelPoints') as HTMLCanvasElement;
    this.chart = new Chart(ctx,{
      type: 'line',
      data: {
        labels: Array.from({ length: 10 }, (_, i) => i),
        datasets: [{
          label: 'Points required to level up',
          data: this.calculatePoints(),
          borderColor: 'blue',
          backgroundColor: 'rgba(0, 0, 255, 0.1)',
          fill: true
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            display: true
          }
        },
        scales: {
          x: {
            title: {
              display: true,
              text: 'Level'
            }
          },
          y: {
            title: {
              display: true,
              text: 'Points'
            }
          }
        }
      }
    });
  }

  calculatePoints(){
    let a = this.gameLevelPolicyForm.get('a')?.value;
    let b = this.gameLevelPolicyForm.get('b')?.value;
    let c = this.gameLevelPolicyForm.get('c')?.value;
    return Array.from({length: 10}, (_, level) => a * Math.pow(b, level*c));
  }

  updateChart(): void {
    this.chart.data.datasets[0].data = this.calculatePoints();
    this.chart.update();
  }

  isFormValid(): boolean {
    return this.subjectType === 'new'
      ? this.subjectForm.valid && this.gameForm.valid && this.gameLevelPolicyForm.valid
      : this.existingSubjectForm.valid && this.gameForm.valid && this.gameLevelPolicyForm.valid;
  }

  submit(inputElement?: HTMLInputElement){
    let startDate = this.gameForm.get('start_date')?.value;
    let endDate = this.gameForm.get('end_date')?.value;

    startDate = this.dateService.formatDate(startDate);
    endDate = this.dateService.formatDate(endDate);

    let simpleRuleObservables = this.importedSimpleRules.map((simpleRule) =>
      this.service.postSimpleRule(
        simpleRule.name,
        simpleRule.repetitions,
        this.gameForm.get('subject_acronym')?.value,
        this.gameForm.get('course')?.value,
        this.gameForm.get('period')?.value,
        simpleRule.evaluableActionId,
        simpleRule.achievementId,
        simpleRule.achievementAssignmentMessage,
        simpleRule.achievementAssignmentOnlyFirstTime,
        simpleRule.achievementAssignmentCondition,
        simpleRule.achievementAssignmentConditionParameters,
        simpleRule.achievementAssignmentUnits,
        simpleRule.achievementAssignmentAssessmentLevel
      )
    );
    let dateRuleObservables = this.importedDateRules.map((dateRule) =>
      this.service.postDateRule(
        dateRule.name,
        dateRule.repetitions,
        this.gameForm.get('subject_acronym')?.value,
        this.gameForm.get('course')?.value,
        this.gameForm.get('period')?.value,
        dateRule.evaluableActionId,
        dateRule.achievementId,
        dateRule.achievementAssignmentMessage,
        dateRule.achievementAssignmentOnlyFirstTime,
        dateRule.achievementAssignmentCondition,
        dateRule.achievementAssignmentConditionParameters,
        dateRule.achievementAssignmentUnits,
        dateRule.achievementAssignmentAssessmentLevel,
        dateRule.startDate,
        dateRule.endDate
      )
    );

    let groupObservable = this.service.postGameGroup(
      this.gameForm.get('subject_acronym')?.value,
      this.gameForm.get('course')?.value,
      this.gameForm.get('period')?.value,
      10
    )

    iif(
      () => this.subjectType === 'new',
      this.service.postSubject(this.subjectForm.get('acronym')?.value,
        this.subjectForm.get('code')?.value,
        this.subjectForm.get('name')?.value,
        this.subjectForm.get('studies')?.value,
        this.subjectForm.get('school')?.value).pipe(
          catchError((error) => {
            alert('Error creating subject: Subject with this acronym, name or code already exists.');
            this.stepper.selectedIndex = 0;
            return throwError(() => error);
          })
      ),
      of(null)
    ).pipe(switchMap( () => {
      return this.service.postGame(this.gameForm.get('subject_acronym')?.value,
        this.gameForm.get('course')?.value,
        this.gameForm.get('period')?.value,
        startDate,
        endDate,
        this.gameLevelPolicyForm.get('a')?.value,
        this.gameLevelPolicyForm.get('b')?.value,
        this.gameLevelPolicyForm.get('c')?.value
        ).pipe(
          catchError((error) => {
            alert('Error creating game: Game with this course and period already exists.');
            this.stepper.selectedIndex = 1;
            return throwError(() => error);
          })
      );
    }),
    switchMap(() => {
      return forkJoin([
        ...simpleRuleObservables,
        ...dateRuleObservables,
        groupObservable
      ]);
    }),
    switchMap(() => {
      if(this.selectedFile)
        return this.service.postImportData(this.gameForm.get('subject_acronym')?.value,
          this.gameForm.get('course')?.value,
          this.gameForm.get('period')?.value,
          this.selectedFile
        );
      else
        return of(null);
    })).subscribe({
      next: () => {
        alert('Game created successfully.');
        this.stepper.reset();
        this.gameLevelPolicyForm.reset({a: 1, b: 1.4, c: 2});
        if (inputElement) this.deleteFile(inputElement);
      },
      error: () => alert('An unexpected error occurred.')
    });
  }
}
