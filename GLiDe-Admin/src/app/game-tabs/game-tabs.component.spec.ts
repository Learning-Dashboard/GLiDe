import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameTabsComponent } from './game-tabs.component';
import {provideHttpClient} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';

describe('GameTabsComponent', () => {
  let component: GameTabsComponent;
  let fixture: ComponentFixture<GameTabsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameTabsComponent],
      providers: [provideAnimationsAsync()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GameTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
