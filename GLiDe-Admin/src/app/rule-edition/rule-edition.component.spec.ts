import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RuleEditionComponent } from './rule-edition.component';
import {provideHttpClient} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';

describe('RuleEditionComponent', () => {
  let component: RuleEditionComponent;
  let fixture: ComponentFixture<RuleEditionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RuleEditionComponent],
      providers: [provideHttpClient(), provideAnimationsAsync()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RuleEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
