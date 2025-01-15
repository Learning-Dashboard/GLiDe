import { Component } from '@angular/core';
import {MatTabsModule} from "@angular/material/tabs";
import {MatIconModule} from '@angular/material/icon';
import {GameCreationComponent} from '../game-creation/game-creation.component';
import {GameEditionComponent} from '../game-edition/game-edition.component';

@Component({
  selector: 'app-game-tabs',
  imports: [
    MatTabsModule,
    MatIconModule,
    GameCreationComponent,
    GameEditionComponent
  ],
  templateUrl: './game-tabs.component.html',
  styleUrl: './game-tabs.component.css'
})
export class GameTabsComponent {

}
