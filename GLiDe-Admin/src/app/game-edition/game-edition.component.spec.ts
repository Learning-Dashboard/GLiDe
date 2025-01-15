import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameEditionComponent } from './game-edition.component';
import {provideHttpClient} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';

describe('GameEditionComponent', () => {
  let component: GameEditionComponent;
  let fixture: ComponentFixture<GameEditionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameEditionComponent],
      providers: [provideHttpClient(), provideAnimationsAsync()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GameEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
