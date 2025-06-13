import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { UsermonitoringComponent } from '../userMonitoring/usermonitoring.component';
import { ProjectmonitoringComponent } from '../projectMonitoring/projectmonitoring.component';
import { GamificationtabsComponent } from '../gamificationTabs/gamificationtabs.component';
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
    MatSidenavModule,
    MatListModule,
    MatToolbarModule,
    UsermonitoringComponent,
    ProjectmonitoringComponent,
    GamificationtabsComponent
  ],
  templateUrl: './teacher-player-metrics.component.html',
  styleUrls: ['./teacher-player-metrics.component.css']
})


export class TeacherPlayerMetricsComponent implements OnInit {
  player_name: string = '';
  isUserReady: boolean = false;
  isProjectReady: boolean = false;
  isGamificationReady: boolean = false;
  currentView: string = 'monitoring'; // 'monitoring' or 'gamification'

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
    }    loadPlayerUsernames(playerName: string): void {
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
                    this.isGamificationReady = true;
                }
            }
        });
    }
  goBackToDashboard(): void {
    this.router.navigate(['/teacher-dashboard']);
  }

  switchView(view: string): void {
    this.currentView = view;
  }
}
