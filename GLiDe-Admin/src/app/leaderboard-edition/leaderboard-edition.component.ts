import {Component, Input} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatCard, MatCardContent, MatCardTitle} from "@angular/material/card";
import {MatCheckbox} from "@angular/material/checkbox";
import {
  MatDatepickerToggle,
  MatDateRangeInput,
  MatDateRangePicker,
  MatEndDate,
  MatStartDate
} from "@angular/material/datepicker";
import {MatFormField, MatHint, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";
import {NgForOf, NgIf} from "@angular/common";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {GamificationEngineService} from '../services/gamification-engine.service';
import {DateFormatService} from '../services/date-format.service';

@Component({
  selector: 'app-leaderboard-edition',
  imports: [
    MatButton,
    MatCard,
    MatCardContent,
    MatCardTitle,
    MatCheckbox,
    MatDateRangeInput,
    MatDateRangePicker,
    MatDatepickerToggle,
    MatEndDate,
    MatFormField,
    MatHint,
    MatInput,
    MatLabel,
    MatOption,
    MatSelect,
    MatStartDate,
    MatSuffix,
    NgForOf,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './leaderboard-edition.component.html',
  styleUrl: './leaderboard-edition.component.css'
})
export class LeaderboardEditionComponent {

  @Input() leaderboards: any;
  @Input() achievements: any;
  @Input() game: any;

  selectedLeaderboard: any;

  constructor(private service: GamificationEngineService, private dateService: DateFormatService) {}

  leaderboardForm: FormGroup = new FormGroup({
    leaderboard: new FormControl,
    achievement: new FormControl,
    name: new FormControl,
    startDate: new FormControl,
    endDate: new FormControl,
    assessmentLevel: new FormControl,
    anonymization: new FormControl,
    studentVisible: new FormControl
  });

  onLeaderboardSelect(){
    let leaderboard = this.leaderboardForm.get('leaderboard')?.value;
    this.selectedLeaderboard = leaderboard;
    this.leaderboardForm.get('name')?.setValue(leaderboard.name);
    this.leaderboardForm.get('startDate')?.setValue(leaderboard.startDate);
    this.leaderboardForm.get('endDate')?.setValue(leaderboard.endDate);
    this.leaderboardForm.get('assessmentLevel')?.setValue(leaderboard.assessmentLevel);
    this.leaderboardForm.get('anonymization')?.setValue(leaderboard.anonymization);
    this.leaderboardForm.get('studentVisible')?.setValue(leaderboard.studentVisible);
    this.leaderboardForm.get('achievement')?.setValue(leaderboard.achievementId);
  }

  editLeaderboard(){
    let startDate = this.leaderboardForm.get('startDate')?.value;
    let endDate = this.leaderboardForm.get('endDate')?.value;

    startDate = this.dateService.formatDate(startDate);
    endDate = this.dateService.formatDate(endDate);

    this.service.updateLeaderboard(
      this.selectedLeaderboard.id,
      this.leaderboardForm.get('name')?.value,
      startDate,
      endDate,
      this.leaderboardForm.get('assessmentLevel')?.value,
      'Subject',
      this.leaderboardForm.get('anonymization')?.value,
      this.leaderboardForm.get('studentVisible')?.value,
      this.game.subjectAcronym,
      this.game.course,
      this.game.period,
      this.leaderboardForm.get('achievement')?.value
    ).subscribe({
      next: (result) => {
        alert('Leaderboard updated successfully.');
        this.selectedLeaderboard = result;
        this.leaderboardForm.get('leaderboard')?.setValue(result);
        for (let leaderboard in this.leaderboards){
          if (this.leaderboards[leaderboard].id === this.selectedLeaderboard.id){
            this.leaderboards[leaderboard] = result;
            break;
          }
        }
      },
      error: () => alert('An unexpected error occurred.')
    });
  }

  deleteLeaderboard(){
    this.service.deleteLeaderboard(this.selectedLeaderboard.id).subscribe({
      next: () => {
        alert('Leaderboard deleted successfully.');
        for (let leaderboard in this.leaderboards){
          if (this.leaderboards[leaderboard].id === this.selectedLeaderboard.id){
            this.leaderboards.splice(leaderboard, 1);
            break;
          }
        }
        this.selectedLeaderboard = null;
        this.leaderboardForm.reset();
      },
      error: () => alert('An unexpected error occurred.')
    });
  }
}
