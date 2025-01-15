import {Component, Input} from '@angular/core';
import {MatCard, MatCardContent, MatCardTitle} from "@angular/material/card";
import {MatFormField, MatHint, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatOption} from "@angular/material/core";
import {MatSelect, MatSelectTrigger} from "@angular/material/select";
import {NgForOf, NgIf} from "@angular/common";
import {FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatCheckbox} from '@angular/material/checkbox';
import {
  MatDatepickerToggle,
  MatDateRangeInput,
  MatDateRangePicker,
  MatEndDate,
  MatStartDate
} from '@angular/material/datepicker';
import {MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {GamificationEngineService} from '../services/gamification-engine.service';
import {DateFormatService} from '../services/date-format.service';

@Component({
  selector: 'app-rule-edition',
  imports: [
    MatCard,
    MatCardContent,
    MatCardTitle,
    MatFormField,
    MatLabel,
    MatOption,
    MatSelect,
    NgForOf,
    ReactiveFormsModule,
    NgIf,
    MatCheckbox,
    MatDateRangeInput,
    MatDateRangePicker,
    MatDatepickerToggle,
    MatEndDate,
    MatHint,
    MatInput,
    MatSelectTrigger,
    MatStartDate,
    MatSuffix,
    MatButton
  ],
  templateUrl: './rule-edition.component.html',
  styleUrl: './rule-edition.component.css'
})
export class RuleEditionComponent {

  @Input() rules: any;
  @Input() ruleType: any;
  @Input() achievements: any;
  @Input() evaluableActions: any;

  selectedRule: any;
  selectedEvaluableAction: any;
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

  ruleForm: FormGroup = new FormGroup({
    rule: new FormControl,
    evaluableAction: new FormControl,
    achievement: new FormControl,
    startDate: new FormControl,
    endDate: new FormControl,
    name: new FormControl,
    repetitions: new FormControl,
    achievementAssignmentMessage: new FormControl,
    onlyFirstTime: new FormControl,
    achievementAssignmentUnits: new FormControl,
    achievementAssignmentCondition: new FormControl,
    achievementAssignmentParameters: new FormArray([new FormControl()])
  });

  get achievementAssignmentParameters(){
    return this.ruleForm.get("achievementAssignmentParameters") as FormArray;
  }
/*
  addAchievementAssignmentParameter(){
    this.achievementAssignmentParameters.push(new FormControl('', Validators.required));
  }

  removeAchievementAssignmentParameter(i: number){
    this.achievementAssignmentParameters.removeAt(i);
  }

 */

  protected conditionChange(){
    if (this.ruleForm.get('achievementAssignmentCondition')?.value !== 'ValueInsideOfRange' && this.ruleForm.get('achievementAssignmentCondition')?.value !== 'ValueOutsideOfRange') {
      const firstValue = this.achievementAssignmentParameters.at(0);
      this.achievementAssignmentParameters.clear();
      this.achievementAssignmentParameters.push(firstValue);
    }
    else if (this.achievementAssignmentParameters.length < 2) {
      this.achievementAssignmentParameters.push(new FormControl('', Validators.required));
    }
  }

  selectRule(){
    let rule = this.ruleForm.get('rule')?.value;
    let achievement = this.selectAchievement(rule.achievementId);
    this.selectedRule = rule;
    this.ruleForm.get('name')?.setValue(rule.name);
    this.ruleForm.get('startDate')?.setValue(rule.startDate);
    this.ruleForm.get('endDate')?.setValue(rule.endDate);
    this.ruleForm.get('achievement')?.setValue(achievement);
    this.ruleForm.get('evaluableAction')?.setValue(rule.evaluableActionId);
    this.ruleForm.get('repetitions')?.setValue(rule.repetitions);
    this.ruleForm.get('achievementAssignmentMessage')?.setValue(rule.achievementAssignmentMessage);
    this.ruleForm.get('onlyFirstTime')?.setValue(rule.achievementAssignmentOnlyFirstTime);
    this.ruleForm.get('achievementAssignmentUnits')?.setValue(rule.achievementAssignmentUnits);
    this.ruleForm.get('achievementAssignmentCondition')?.setValue(rule.achievementAssignmentCondition);
    this.ruleForm.get('achievementAssignmentParameters')?.setValue(rule.achievementAssignmentConditionParameters);
    this.selectEvaluableAction();
  }

  selectEvaluableAction(){
    this.selectedEvaluableAction = this.evaluableActions.find((evaluableAction: any) =>
      evaluableAction.id === this.ruleForm.get('evaluableAction')?.value);
  }

  selectAchievement(achievementId: any){
    return this.achievements.find((achievement: any) =>
      achievement.id === achievementId);
  }

  editRule(){
    let rule = this.ruleForm.get('rule')?.value;
    let repetitions = Math.round(this.ruleForm.get('repetitions')?.value);
    let points = Math.round(this.ruleForm.get('achievementAssignmentUnits')?.value);
    let evaluableAction = this.evaluableActions.find((evaluableAction: any) => evaluableAction.id === this.ruleForm.get('evaluableAction')?.value);
    let observable;

    if (this.ruleType === 'simple')
      observable = this.service.updateSimpleRule(
        rule.id,
        this.ruleForm.get('name')?.value,
        repetitions,
        rule.gameSubjectAcronym,
        rule.gameCourse,
        rule.gamePeriod,
        this.ruleForm.get('evaluableAction')?.value,
        this.ruleForm.get('achievementAssignmentMessage')?.value,
        this.ruleForm.get('onlyFirstTime')?.value,
        this.ruleForm.get('achievementAssignmentCondition')?.value,
        this.ruleForm.get('achievementAssignmentParameters')?.value,
        points,
        evaluableAction.assessmentLevel
      );
    else {
      let startDate = this.ruleForm.get('startDate')?.value;
      let endDate = this.ruleForm.get('endDate')?.value;

      startDate = this.dateService.formatDate(startDate);
      endDate = this.dateService.formatDate(endDate);

      observable = this.service.updateDateRule(
        rule.id,
        this.ruleForm.get('name')?.value,
        repetitions,
        rule.gameSubjectAcronym,
        rule.gameCourse,
        rule.gamePeriod,
        this.ruleForm.get('evaluableAction')?.value,
        this.ruleForm.get('achievementAssignmentMessage')?.value,
        this.ruleForm.get('onlyFirstTime')?.value,
        this.ruleForm.get('achievementAssignmentCondition')?.value,
        this.ruleForm.get('achievementAssignmentParameters')?.value,
        points,
        evaluableAction.assessmentLevel,
        startDate,
        endDate
      );
    }

    observable.subscribe({
      next: (result) => {
        alert('Rule updated successfully.');
        this.selectedRule = result;
        this.ruleForm.get('rule')?.setValue(result);
        for (let rule in this.rules){
          if (this.rules[rule].id === this.selectedRule.id) {
            this.rules[rule] = result;
            break;
          }
        }
      },
      error: () => alert('An unexpected error occurred.')
    });
  }

  deleteResultRule(){
    alert('Leaderboard deleted successfully.');
    for (let rule in this.rules) {
      if (this.rules[rule].id === this.selectedRule.id) {
        this.rules.splice(rule, 1);
        break;
      }
    }
    this.selectedRule = null;
    this.ruleForm.reset();
  }

  deleteRule(){
    if (this.ruleType === 'date'){
      this.service.deleteDateRule(this.selectedRule.id).subscribe({
        next: () => {
          this.deleteResultRule();
        },
        error: () => alert('An unexpected error occurred.')
      })
    } else {
      this.service.deleteSimpleRule(this.selectedRule.id).subscribe({
        next: () => {
          this.deleteResultRule();
        },
        error: () => alert('An unexpected error occurred.')
      })
    }
  }

}
