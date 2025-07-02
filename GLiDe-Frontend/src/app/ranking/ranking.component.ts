import {Component, Input, ViewChild} from '@angular/core';
import {MatTableModule, MatTable} from '@angular/material/table';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { RankingDatasource, TableItem } from './ranking-datasource';
import {LearningdashboardService} from "../services/learningdashboard.service";
import {MatProgressBar} from "@angular/material/progress-bar";

@Component({
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  styleUrl: './ranking.component.css',
  standalone: true,
  imports: [MatTableModule, MatPaginatorModule, MatSortModule, MatProgressBar]
})

export class RankingComponent {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<TableItem>;

  @Input() item: any;

  private dataSource : any = [];
  private individualPlayerName : any;
  private teamPlayerName : any;
  private data: any = [];
  private list: TableItem[] = [];
  protected position = 0;
  private maxPoints: any;


  private studentNicknamesMap = new Map<string, string>();

  constructor(private service: LearningdashboardService) {  }

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['position', 'name', 'points'];

  ngOnChanges(){
    // Load student nicknames first
    this.loadStudentNicknames();
    
    this.dataSource = [];
    this.data = [];
    this.list = [];

    let leaderboardId = this.item.id;
    let anonymization = this.item.anonymization;

    this.service.getLeaderboardResults(leaderboardId).subscribe((res) => {
      this.data = res;
      this.position = 0
      this.data = this.data[0].leaderboardResults;

      this.individualPlayerName = localStorage.getItem("individualPlayername");
      this.teamPlayerName = localStorage.getItem("teamPlayername");

      for (let a in this.data) {
        this.position = this.position + 1;
        if (this.position == 1) this.maxPoints = this.data[a].points;
        let percent = (this.data[a].points / this.maxPoints) * 100;
        let name: any;
        if ((this.data[a].playername == this.teamPlayerName || this.data[a].playername == this.individualPlayerName) || anonymization == "None") name = this.getDisplayName(this.data[a]);
        else if (anonymization == "Partial" && this.position < 4) name = this.getDisplayName(this.data[a]);
        else name = "-";
        this.list.push({name: name, points: this.data[a].achievementunits, position: this.position, percent: percent});
      }

      this.data = this.list;

      this.dataSource = new RankingDatasource(this.data);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.table.dataSource = this.dataSource;
    })
  }

  public getDisplayName(player: any): string {
    if (player?.nickname) return player.nickname;

    const name = player?.playername ?? player?.name ?? '';
    return this.studentNicknamesMap.get(name) || name;
  }

  private loadStudentNicknames() {
    const project = localStorage.getItem('project');
    if (project) {
      this.service.getProjectStudentsWithNicknames(project).subscribe((students: any) => {
        if (Array.isArray(students)) {
          students.forEach((student: any) => {
            if (student.nickname) {
              this.studentNicknamesMap.set(student.name, student.nickname);
            }
          });
        }
      });
    }
  }
}
