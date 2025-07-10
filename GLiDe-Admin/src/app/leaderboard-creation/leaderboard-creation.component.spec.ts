import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeaderboardCreationComponent } from './leaderboard-creation.component';
import {HttpResponse, provideHttpClient} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {GamificationEngineService} from '../services/gamification-engine.service';
import {of} from 'rxjs';
import {By} from '@angular/platform-browser';

describe('LeaderboardCreationComponent', () => {
  let component: LeaderboardCreationComponent;
  let fixture: ComponentFixture<LeaderboardCreationComponent>;
  let mockService: jasmine.SpyObj<GamificationEngineService>;

  beforeEach(async () => {
    mockService = jasmine.createSpyObj('GamificationEngineService', [
      'getGames',
      'getAchievements',
      'postLeaderboard'
    ]);

    const mockCreated = new HttpResponse<Object>({
      status: 201
    });
    mockService.getGames.and.returnValue(of([{
      subjectAcronym: 'AMEP',
      course: 2023,
      period: 'Quadrimester2',
      state: 'Active'
    }]));
    mockService.getAchievements.and.returnValue(of([{
      id: '1',
      name: 'Achievement 1',
      category: 'Points'
    }]));
    mockService.postLeaderboard.and.returnValue(of(mockCreated));

    await TestBed.configureTestingModule({
      imports: [LeaderboardCreationComponent],
      providers: [provideHttpClient(), provideAnimationsAsync(), { provide: GamificationEngineService, useValue: mockService }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LeaderboardCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch active games on initialization', () => {
    expect(mockService.getGames).toHaveBeenCalled();
    expect(component.games).toEqual([{ subjectAcronym: 'AMEP', course: 2023, period: 'Quadrimester2', state: 'Active' }]);
  });

  it('should fetch achievements on initialization', () => {
    expect(mockService.getAchievements).toHaveBeenCalled();
    expect(component.achievements).toEqual([{ id: '1', name: 'Achievement 1', category: 'Points' }]);
  });

  it('should reset the form when resetForm is called', () => {
    component.form.patchValue({
      game: { subjectAcronym: 'AMEP', course: 2023, period: 'Quadrimester2' },
      name: 'Test Leaderboard',
      startDate: new Date(),
      endDate: new Date(),
      assessmentLevel: 'Individual',
      anonymization: 'Full',
      studentVisible: true,
    });

    component.resetForm();

    expect(component.form.value).toEqual({
      game: null,
      achievement: null,
      name: null,
      startDate: null,
      endDate: null,
      assessmentLevel: null,
      anonymization: null,
      studentVisible: false,
    });
  });

  it('should call postLeaderboard on form submission with valid data', () => {
    const mockGame = { subjectAcronym: 'AMEP', course: 2023, period: 'Quadrimester2' };
    const mockStartDate = new Date('2024-01-01');
    const mockEndDate = new Date('2024-12-31');
    const mockFormData = {
      name: 'Test Leaderboard',
      assessmentLevel: 'Team',
      anonymization: 'Partial',
      studentVisible: true,
      game: mockGame,
      achievement: 1,
      startDate: mockStartDate,
      endDate: mockEndDate,
    };

    component.form.patchValue(mockFormData);

    const compiledStartDate = mockStartDate.toISOString().split('T')[0];
    const compiledEndDate = mockEndDate.toISOString().split('T')[0];

    component.onSubmit();

    expect(mockService.postLeaderboard).toHaveBeenCalledWith(
      'Test Leaderboard',
      compiledStartDate,
      compiledEndDate,
      'Team',
      'Subject',
      'Partial',
      true,
      'AMEP',
      2023,
      'Quadrimester2',
      1
    );
  });

  it('should disable the submit button if the form is invalid', () => {
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(submitButton.nativeElement.disabled).toBeTrue();

    component.form.patchValue({
      game: { subjectAcronym: 'AMEP', course: 2023, period: 'Quadrimester2' },
      achievement: 1,
      name: 'Valid Leaderboard',
      startDate: new Date(),
      endDate: new Date(),
      assessmentLevel: 'Team',
      anonymization: 'Full',
      studentVisible: true,
    });

    fixture.detectChanges();

    expect(submitButton.nativeElement.disabled).toBeFalse();
  });
});
