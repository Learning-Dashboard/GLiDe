import {Component, Input} from '@angular/core';
import Chart from "chart.js/auto";
import {LearningdashboardService} from "../services/learningdashboard.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-leaderboard',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.css'
})
export class LeaderboardComponent {

  @Input() item: any;

  protected chart: any;

  private leaderboardResultsData: any = [];
  private leaderboardResults: any = [];
  protected validLeaderboard = false;
  private initialized = false;

  // Map to store student nicknames
  private studentNicknamesMap = new Map<string, string>();

  constructor(private service: LearningdashboardService){}

  ngAfterViewInit(){
    this.validLeaderboard = true;
    // Load student nicknames first
    this.loadStudentNicknames();
    this.drawLeaderboard();
    this.initialized = true;
  }

  ngOnChanges(){
    if(this.initialized){
      this.validLeaderboard = true;
      this.drawLeaderboard();
    }
  }

  drawLeaderboard(){

    const leaderboardId = this.item.id;
    let anonymization = this.item.anonymization;

    if (this.chart) this.chart.destroy();

    this.service.getLeaderboardResults(leaderboardId).subscribe((res) => {
      this.leaderboardResultsData = res;
      this.leaderboardResults = this.leaderboardResultsData[0].leaderboardResults;

      let name = ['-', '-', '-'];
      let achievementUnit = ['-', '-', '-'];
      let avatar = ['-', '-', '-'];

      const position1 = this.leaderboardResults.filter((x: { position: number}) => x.position === 1);
      const position2 = this.leaderboardResults.filter((x: { position: number}) => x.position === 2);
      const position3 = this.leaderboardResults.filter((x: { position: number}) => x.position === 3);

      this.validLeaderboard = position1.length === 1 && position2.length === 1 && position3.length === 1;
      if (this.validLeaderboard) {
        if (anonymization !== 'Full'){
          name[1] = this.buildNameWithNickname(position1[0]);
          name[0] = this.buildNameWithNickname(position2[0]);
          name[2] = this.buildNameWithNickname(position3[0]);
        }
        else {
          let individualPlayername = localStorage.getItem("individualPlayername");
          if (position1[0].playername === individualPlayername) name[1] = this.buildNameWithNickname(position1[0]);
          if (position2[0].playername === individualPlayername) name[0] = this.buildNameWithNickname(position2[0]);
          if (position3[0].playername === individualPlayername) name[2] = this.buildNameWithNickname(position3[0]);
        }

        achievementUnit[1] = position1[0].achievementunits;
        avatar[1] = position1[0].playerimage;

        achievementUnit[0] = position2[0].achievementunits;
        avatar[0] = position2[0].playerimage;

        achievementUnit[2] = position3[0].achievementunits;
        avatar[2] = position3[0].playerimage;

        this.chart = printLeaderboard(name, achievementUnit, avatar, leaderboardId);
      }
    })

    function base64ToBlob(base64: string, mimeType: any) {
      const byteCharacters = atob(base64);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      return new Blob([byteArray], { type: mimeType });
    }

    function printLeaderboard(name: string[], achievementUnit: string[], avatar: string[], id: any) {

      const data = {
        labels: ['2','1','3'],
        datasets: [{
          label: 'My First Dataset',
          data: [30, 40, 20],
          backgroundColor: [
            'rgba(59,130,246,0.8)',
            'rgba(29,78,216,0.8)',
            'rgba(147,197,253,0.8)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(201, 203, 207, 0.2)'
          ],
          borderWidth: 0,
          barPercentage: 1,
          categoryPercentage: 0.9,
          borderRadius: {
            topLeft: 10,
            topRight: 10
          },
          positions: ['2','1','3'],
          names: name,
          points: achievementUnit,
          image: avatar
        }]
      }

      const bgImage = {
        id: 'bgImage',
        afterDatasetsDraw(chart: { getDatasetMeta?: any; ctx?: any; data?: any; chartArea?: any; scales?: any}) {
          const {ctx, data, scales} = chart;
          ctx.save();
          data.datasets[0].image.forEach((image: string, index: number) => {
            const xPos = chart.getDatasetMeta(0).data[index].x;
            const yPos = chart.getDatasetMeta(0).data[index].y;

            const b64 = image;

            ctx.font = 'bolder 80px sans-serif';
            ctx.fillStyle = 'white';
            ctx.textAlign = 'center'
            ctx.fillText(data.datasets[0].positions[index], scales.x.getPixelForValue(index), yPos + 100);

            if (image != '-') {
              const mime = 'image/png';
              const blob = base64ToBlob(b64, mime);

              createImageBitmap(blob).then((imageBitmap) => {
                ctx.drawImage(imageBitmap, xPos - 50, yPos - 140, 100, 100);
              });

              ctx.font = 'bolder 20px sans-serif';
              ctx.fillStyle = 'black';
              ctx.textAlign = 'center'
              ctx.fillText(data.datasets[0].names[index], scales.x.getPixelForValue(index), yPos - 20);

              ctx.font = 'bolder 15px sans-serif';
              ctx.fillStyle = 'gray';
              ctx.textAlign = 'center'
              ctx.fillText(data.datasets[0].points[index] + " points", scales.x.getPixelForValue(index), yPos - 5);
            }
          })
        }
      };

      const elementId = 'canvasLeaderboard_' + id;

      let ctx: any;
      ctx = document.getElementById(elementId);

      return new Chart(ctx, {
        type: 'bar',
        data: data,
        plugins: [bgImage],
        options: {
          maintainAspectRatio: false,
          resizeDelay: 200,
          events: [],
          plugins: {
            legend: {
              display: false
            },
            tooltip: {
              enabled: false
            }
          },
          scales: {
            x: {
              grid: {
                drawOnChartArea: false,
                drawTicks: false
              },
              border: {
                display: false
              },
              ticks: {
                display: false
              }
            },
            y: {
              beginAtZero: true,
              grid: {
                drawOnChartArea: false,
                drawTicks: false
              },
              border: {
                display: false
              },
              ticks: {
                display: false
              },
              grace: 20
            }
          }
        },
      });
    }
  }

  private loadStudentNicknames() {
    this.service.getAllStudentNicknames().subscribe((students: any) => {
      if (Array.isArray(students)) {
        students.forEach((student: any) => {
          if (student.nickname) {
            this.studentNicknamesMap.set(student.name, student.nickname);
          }
        });
      }
    });
  }

  public getDisplayName(player: any): string {
    if (player?.nickname) return player.nickname;

    const name = player?.playername ?? player?.name ?? '';
    return this.studentNicknamesMap.get(name) || name;
  }

  private buildNameWithNickname(player: any): string {
    const displayName = this.getDisplayName(player);
    const originalName = player?.playername ?? player?.name ?? '';
    return displayName === originalName ? displayName : `${originalName} (${displayName})`;
  }
}
