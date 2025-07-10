import { ApplicationConfig } from '@angular/core';
import { provideRouter, withRouterConfig } from '@angular/router';
import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {provideCharts, withDefaultRegisterables} from "ng2-charts";
import {provideHttpClient} from "@angular/common/http";
import {provideToastr} from "ngx-toastr";

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes, withRouterConfig({ onSameUrlNavigation: 'reload'})), provideAnimationsAsync(), provideCharts(withDefaultRegisterables()), provideHttpClient(), provideToastr()]
};
