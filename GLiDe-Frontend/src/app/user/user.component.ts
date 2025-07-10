import {Component, ElementRef, inject} from '@angular/core';

import {ReactiveFormsModule, FormBuilder, Validators, FormsModule} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import {LearningdashboardService} from "../services/learningdashboard.service";
import {forkJoin, switchMap} from "rxjs";
import {DomSanitizer} from "@angular/platform-browser";
import {NgForOf, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";




@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrl: './user.component.css',
  standalone: true,
  imports: [
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    MatIconModule,
    ReactiveFormsModule,
    FormsModule,
    NgIf,
    NgForOf
  ]
})
export class UserComponent {
  private fb = inject(FormBuilder);
  protected result : any;
  protected gamification : any;
  protected players : any;
  protected teams : any;
  protected nickname: string = '';

  protected selectedPlayer: any;
  protected selectedFileName: string = '';
  protected selectedTeamLogoFileName: string = '';

  addressForm = this.fb.group({
    company: null,
    firstName: [null, Validators.required],
    lastName: [null, Validators.required],
    address: [null, Validators.required],
    address2: null,
    city: [null, Validators.required],
    state: [null, Validators.required],
    user: [null, Validators.required],
    postalCode: [null, Validators.compose([
      Validators.required, Validators.minLength(5), Validators.maxLength(5)])
    ],
    shipping: ['free', Validators.required]
  });

  hasUnitNumber = false;

  states = [
    {name: 'Alabama', abbreviation: 'AL'},
    {name: 'Alaska', abbreviation: 'AK'},
    {name: 'American Samoa', abbreviation: 'AS'},
    {name: 'Arizona', abbreviation: 'AZ'},
    {name: 'Arkansas', abbreviation: 'AR'},
    {name: 'California', abbreviation: 'CA'},
    {name: 'Colorado', abbreviation: 'CO'},
    {name: 'Connecticut', abbreviation: 'CT'},
    {name: 'Delaware', abbreviation: 'DE'},
    {name: 'District Of Columbia', abbreviation: 'DC'},
    {name: 'Federated States Of Micronesia', abbreviation: 'FM'},
    {name: 'Florida', abbreviation: 'FL'},
    {name: 'Georgia', abbreviation: 'GA'},
    {name: 'Guam', abbreviation: 'GU'},
    {name: 'Hawaii', abbreviation: 'HI'},
    {name: 'Idaho', abbreviation: 'ID'},
    {name: 'Illinois', abbreviation: 'IL'},
    {name: 'Indiana', abbreviation: 'IN'},
    {name: 'Iowa', abbreviation: 'IA'},
    {name: 'Kansas', abbreviation: 'KS'},
    {name: 'Kentucky', abbreviation: 'KY'},
    {name: 'Louisiana', abbreviation: 'LA'},
    {name: 'Maine', abbreviation: 'ME'},
    {name: 'Marshall Islands', abbreviation: 'MH'},
    {name: 'Maryland', abbreviation: 'MD'},
    {name: 'Massachusetts', abbreviation: 'MA'},
    {name: 'Michigan', abbreviation: 'MI'},
    {name: 'Minnesota', abbreviation: 'MN'},
    {name: 'Mississippi', abbreviation: 'MS'},
    {name: 'Missouri', abbreviation: 'MO'},
    {name: 'Montana', abbreviation: 'MT'},
    {name: 'Nebraska', abbreviation: 'NE'},
    {name: 'Nevada', abbreviation: 'NV'},
    {name: 'New Hampshire', abbreviation: 'NH'},
    {name: 'New Jersey', abbreviation: 'NJ'},
    {name: 'New Mexico', abbreviation: 'NM'},
    {name: 'New York', abbreviation: 'NY'},
    {name: 'North Carolina', abbreviation: 'NC'},
    {name: 'North Dakota', abbreviation: 'ND'},
    {name: 'Northern Mariana Islands', abbreviation: 'MP'},
    {name: 'Ohio', abbreviation: 'OH'},
    {name: 'Oklahoma', abbreviation: 'OK'},
    {name: 'Oregon', abbreviation: 'OR'},
    {name: 'Palau', abbreviation: 'PW'},
    {name: 'Pennsylvania', abbreviation: 'PA'},
    {name: 'Puerto Rico', abbreviation: 'PR'},
    {name: 'Rhode Island', abbreviation: 'RI'},
    {name: 'South Carolina', abbreviation: 'SC'},
    {name: 'South Dakota', abbreviation: 'SD'},
    {name: 'Tennessee', abbreviation: 'TN'},
    {name: 'Texas', abbreviation: 'TX'},
    {name: 'Utah', abbreviation: 'UT'},
    {name: 'Vermont', abbreviation: 'VT'},
    {name: 'Virgin Islands', abbreviation: 'VI'},
    {name: 'Virginia', abbreviation: 'VA'},
    {name: 'Washington', abbreviation: 'WA'},
    {name: 'West Virginia', abbreviation: 'WV'},
    {name: 'Wisconsin', abbreviation: 'WI'},
    {name: 'Wyoming', abbreviation: 'WY'}
  ];

  onSubmit(): void {
    this.saveUserData();
  }

  onPlayerSelected(playername: string): void {
    this.selectedPlayer = playername;
    this.saveUserData(); 
  }
  

  saveUserData(): void {
    const selected = this.result.find((x: { playername: string }) => x.playername === this.selectedPlayer);

    if (!selected) return;

    function normalizeName(str: string): string {
      return str.normalize("NFD").replace(/[\u0300-\u036f]/g, ""); 
    }
    
    const normalizedUsername = normalizeName(selected.learningdashboardUsername);

    let points = this.result.find((x: { playername: string}) => x.playername === this.selectedPlayer).points;
    //categoryName = metricsWithCategories.find((x: { externalId: string}) => x.externalId === this.selectedMetrics[metric]).categoryName;
    let level = this.result.find((x: { playername: string}) => x.playername === this.selectedPlayer).level;
    localStorage.setItem('selectedPlayer', this.selectedPlayer);

    localStorage.setItem('username', normalizedUsername);
    localStorage.setItem('githubUsername', selected.githubUsername);
    localStorage.setItem('taigaUsername', selected.taigaUsername);
    localStorage.setItem('project', selected.project);
    localStorage.setItem('teamPlayername', selected.teamPlayername);
    localStorage.setItem('individualPlayername', selected.playername);
    localStorage.setItem('nickname', selected.nickname || '');

    let individualPlayername: any;

    individualPlayername = localStorage.getItem('individualPlayername');

    this.service.getPlayerGamification(individualPlayername).subscribe((res) => {
      this.gamification = res;
      localStorage.setItem('gameSubjectAcronym', this.gamification.gameSubjectAcronym);
      localStorage.setItem('gameCourse', this.gamification.gameCourse);
      localStorage.setItem('gamePeriod', this.gamification.gamePeriod);
    });
  }

  logOut() {
    localStorage.clear();
    this.router.navigate(['/login']);
    this.toastr.success('Successfully logged out','Logged out');
  }

  constructor(private service: LearningdashboardService, private sanitizer: DomSanitizer, public router: Router, private toastr: ToastrService) {}

  ngOnInit(): void {
    let idToken = localStorage.getItem('idToken');
    if (idToken) {
      this.service.getStudentPlayers(idToken).pipe(switchMap((res) => {
        this.result = res;

        //this.metrics = this.result.map((item: any) => item.value);
        //this.dates = this.result.map((item: any) => item.date);

        return forkJoin({
          players: forkJoin(this.result.map((player:any) => this.service.getIndividualPlayer(player.playername))),
          teams: forkJoin(this.result.map((player:any) => this.service.getTeamPlayer(player.teamPlayername)))
        });

      })).subscribe(({players, teams}) => {
        this.players = players;
        for (let player in this.players){
          this.players[player].avatar = this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + this.players[player].avatar);
        }
        this.teams = teams;
        for (let team in this.teams){
          this.teams[team].logo = this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + this.teams[team].logo);
        }

        let selectedPlayer = localStorage.getItem('selectedPlayer');
        if (selectedPlayer) this.selectedPlayer = selectedPlayer;
        else if (this.result.length !== 0){
          this.selectedPlayer = this.result[0].playername;
          this.saveUserData();
        }
        const selected = this.result.find((x: any) => x.playername === this.selectedPlayer);
        if (selected?.nickname) {
          this.nickname = selected.nickname;
        }
      });
    }
  }

  updateNickname() {
    const idToken = localStorage.getItem('idToken');
    if (!idToken || !this.nickname) return;

    this.service.updateNickname(idToken, this.nickname).subscribe({
      next: () => {
        localStorage.setItem('nickname', this.nickname);
        
        const selectedIndex = this.result.findIndex((x: any) => x.playername === this.selectedPlayer);
        if (selectedIndex !== -1) {
          this.result[selectedIndex].nickname = this.nickname;
        }
      },
      error: () => {
        this.toastr.error('Error actualitzant el nickname');
      }
    });
  }

  onAvatarSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    this.selectedFileName = file.name;
    const reader = new FileReader();

    reader.onload = () => {
      const base64Avatar = (reader.result as string).split(',')[1]; 
      const playername = this.selectedPlayer;

      this.service.updateIndividualPlayerAvatar(playername, base64Avatar).subscribe({
        next: () => {
          alert('Avatar updated successfully!');
          const playerIndex = this.players.findIndex((p: any) => p.playername === playername);
          if (playerIndex !== -1) {
            this.players[playerIndex].avatar = this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + base64Avatar);
          }
        },
        error: (err) => {
          console.error('Error updating avatar:', err);
          alert('Error updating avatar');
        }
      });
    };

    reader.readAsDataURL(file); 
  }

  onTeamLogoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    this.selectedTeamLogoFileName = file.name;
    const reader = new FileReader();

    reader.onload = () => {
      const base64Logo = (reader.result as string).split(',')[1];
      const teamname = this.result.find((x: any) => x.playername === this.selectedPlayer)?.teamPlayername;

      if (teamname) {
        this.service.updateTeamPlayerLogo(teamname, base64Logo).subscribe({
          next: () => {
            alert('Logo updated successfully!');
            const teamIndex = this.teams.findIndex((t: any) => t.playername === teamname);
            if (teamIndex !== -1) {
              console.log('Updating logo for team:', teamname);
              this.teams[teamIndex].logo = this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + base64Logo);
            } else {
              console.log('Team not found for logo update:', teamname);
            }
          },
          error: (err) => {
            console.error('Error updating logo:', err);
            alert('Error updating logo');
          }
        });
      }
    };
    reader.readAsDataURL(file);
  }
}
