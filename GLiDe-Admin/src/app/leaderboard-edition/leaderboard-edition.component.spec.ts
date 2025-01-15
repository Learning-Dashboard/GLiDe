import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeaderboardEditionComponent } from './leaderboard-edition.component';
import {provideHttpClient} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';

describe('LeaderboardEditionComponent', () => {
  let component: LeaderboardEditionComponent;
  let fixture: ComponentFixture<LeaderboardEditionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LeaderboardEditionComponent],
      providers: [provideHttpClient(), provideAnimationsAsync()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LeaderboardEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
