import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { UsermonitoringComponent } from '../userMonitoring/usermonitoring.component';
import { ProjectmonitoringComponent } from '../projectMonitoring/projectmonitoring.component';
import { LearningdashboardService } from '../services/learningdashboard.service';


@Component({
  selector: 'app-teacher-player-metrics',
  standalone: true,
  imports: [
    CommonModule,
    MatTabsModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    UsermonitoringComponent,
    ProjectmonitoringComponent
  ],
  templateUrl: './teacher-player-metrics.component.html',
  styleUrls: ['./teacher-player-metrics.component.css']
})


export class TeacherPlayerMetricsComponent implements OnInit {
  player_name: string = '';
  isUserReady: boolean = false;
  isProjectReady: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private learningDashboardService: LearningdashboardService
  ) {}

    ngOnInit(): void {
        this.player_name = this.route.snapshot.paramMap.get('playername') || '';

        if (this.player_name) {
            localStorage.setItem("selectedPlayer", this.player_name);
            localStorage.setItem("individualPlayername", this.player_name);
            this.loadPlayerUsernames(this.player_name);
        } else {
            console.warn("No s'ha pogut obtenir el nom del jugador des de la ruta.");
        }
    }

    loadPlayerUsernames(playerName: string): void {
        this.learningDashboardService.getDetailedIndividualPlayer(playerName).subscribe({
            next: (player: any) => {
                if (player.githubUsername && player.taigaUsername && player.project) {
                    localStorage.setItem("username", player.learningdashboardUsername);
                    localStorage.setItem("githubUsername", player.githubUsername);
                    localStorage.setItem("taigaUsername", player.taigaUsername);
                    localStorage.setItem("project", player.project);
                    localStorage.setItem("teamPlayername", player.project);

                    this.isUserReady = true;
                    this.isProjectReady = true;
                }
            }
        });
    }

  goBackToDashboard(): void {
    this.router.navigate(['/teacher-dashboard']);
  }
}
