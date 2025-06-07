import { Routes } from '@angular/router';
import {MonitoringtabsComponent} from "./monitoringTabs/monitoringtabs.component";
import {GamificationtabsComponent} from "./gamificationTabs/gamificationtabs.component";
import {UserComponent} from "./user/user.component";
import {LoginComponent} from "./login/login.component";
import {TeacherDashboardComponent} from "./teacher-dashboard/teacher-dashboard.component";
import {LoginAuthService} from "./services/loginAuth.service";
import {ProfileAuthService} from "./services/profileAuth.service";
import {NoAuthService} from "./services/noAuth.service";
import {TeacherAuthService} from "./services/teacherAuth.service";

export const routes: Routes = [
  {path: 'monitoring', component: MonitoringtabsComponent, canActivate: [LoginAuthService, ProfileAuthService]},
  {path: 'gamification', component: GamificationtabsComponent, canActivate: [LoginAuthService, ProfileAuthService]},
  {path: 'profile', component: UserComponent, canActivate: [LoginAuthService]},
  {path: 'teacher-dashboard', component: TeacherDashboardComponent, canActivate: [TeacherAuthService]},
  {path: 'login', component: LoginComponent, canActivate: [NoAuthService]},
  {path: '', redirectTo: 'login', pathMatch: 'full'}
];
