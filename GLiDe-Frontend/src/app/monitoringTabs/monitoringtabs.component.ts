import { Component } from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {MatTabsModule} from '@angular/material/tabs';
import {UsermonitoringComponent} from "../userMonitoring/usermonitoring.component";
import {ProjectmonitoringComponent} from "../projectMonitoring/projectmonitoring.component";
import {CommonModule} from "@angular/common";
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';





@Component({
  selector: 'app-monitoringtabs',
  standalone: true,
  imports: [MatTabsModule, MatIconModule, UsermonitoringComponent, ProjectmonitoringComponent, CommonModule],
  templateUrl: './monitoringtabs.component.html',
  styleUrl: './monitoringtabs.component.css'
})
export class MonitoringtabsComponent {
  username: string | null = null;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.username = localStorage.getItem('username');
    console.log('Usuario en Monitoringtabs:', this.username);

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.username = localStorage.getItem('username');
        console.log('Usuario actualizado en Monitoringtabs:', this.username);
      });
  }
}

