import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class TeacherAuthService {

  constructor(public router: Router, private toastr: ToastrService) { }

  canActivate(): boolean {
    let user = localStorage.getItem('loggedUser');
    let userType = localStorage.getItem('userType');
    
    if (user && userType === 'teacher') {
      let token = JSON.parse(user);
      let currentDate = Math.floor(Date.now() / 1000);
      if(!token.exp || token.exp < currentDate){
        localStorage.clear();
        this.router.navigate(['login']);
        this.toastr.info('Log in again to continue', 'Session timed out');
        return false;
      }
      return true;
    }
    
    if (userType === 'student') {
      this.router.navigate(['profile']);
      this.toastr.error('Access denied. Teacher privileges required.', 'Unauthorized');
      return false;
    }
    
    this.router.navigate(['login']);
    this.toastr.error('Log in as a teacher to access this content', 'Not available');
    return false;
  }
}
