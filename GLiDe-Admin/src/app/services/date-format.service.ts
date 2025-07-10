import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DateFormatService {

  constructor() { }

  formatDate(date: any): String {
    if(date instanceof Date) {
      date.setMinutes(date.getMinutes() - date.getTimezoneOffset())
      return date.toJSON().substring(0, 10);
    }
    else return date;
  }
}
