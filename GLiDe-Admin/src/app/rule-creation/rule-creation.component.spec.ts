import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RuleCreationComponent } from './rule-creation.component';
import {HttpResponse, provideHttpClient} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {GamificationEngineService} from '../services/gamification-engine.service';
import {of} from 'rxjs';

describe('RuleCreationComponent', () => {
  let component: RuleCreationComponent;
  let fixture: ComponentFixture<RuleCreationComponent>;
  let mockService: jasmine.SpyObj<GamificationEngineService>;

  beforeEach(async () => {
    mockService = jasmine.createSpyObj('GamificationEngineService', [
      'getEvaluableActions',
      'getGames',
      'getAchievements',
      'postSimpleRule',
      'postDateRule'
    ]);

    const mockCreated = new HttpResponse<Object>({
      status: 201
    });
    mockService.getEvaluableActions.and.returnValue(of([{
      id: 'LDIM-Student_tasks',
      description: 'Percentage of tasks assigned to a student with respect to the total number of tasks in the project',
      assessmentLevel: 'Individual'
    }]));
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
    mockService.postDateRule.and.returnValue(of(mockCreated));
    mockService.postSimpleRule.and.returnValue(of(mockCreated));

    await TestBed.configureTestingModule({
      imports: [RuleCreationComponent],
      providers: [provideHttpClient(), provideAnimationsAsync(), { provide: GamificationEngineService, useValue: mockService }]
    }).compileComponents();

    fixture = TestBed.createComponent(RuleCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with form and load data from the service', () => {
    fixture.detectChanges();

    expect(component.games.length).toBe(1);
    expect(component.games[0].subjectAcronym).toBe('AMEP');

    expect(component.evaluableActions.length).toBe(1);
    expect(component.evaluableActions[0].id).toBe('LDIM-Student_tasks');

    expect(component.achievements.length).toBe(1);
    expect(component.achievements[0].name).toBe('Achievement 1');
  });

  it('should initialize the form with default values', () => {
    expect(component.form.get('ruleType')?.value).toBe('simple');
    expect(component.form.get('onlyFirstTime')?.value).toBeFalse();
  });

  it('should reset the form after submission', () => {
    spyOn(component.form, 'reset').and.callThrough();
    component.form.get('ruleType')?.setValue('date');
    component.resetForm();

    expect(component.form.reset).toHaveBeenCalled();
    expect(component.form.get('ruleType')?.value).toBe('simple');
    expect(component.form.get('onlyFirstTime')?.value).toBeFalse();
  });

  it('should call postSimpleRule with correct parameters on simple rule submission', () => {
    fixture.detectChanges();
    component.form.setValue({
      game: { subjectAcronym: 'AMEP', course: 2023, period: 'Quadrimester2' },
      evaluableAction: { id: 'LDIM-Student_tasks', assessmentLevel: 'Individual' },
      achievement: 1,
      ruleType: 'simple',
      startDate: null,
      endDate: null,
      name: 'Rule 1',
      repetitions: 1,
      achievementAssignmentMessage: 'Test Message',
      onlyFirstTime: true,
      achievementAssignmentUnits: 10,
      achievementAssignmentCondition: 'ValueGreaterThan',
      achievementAssignmentParameters: [5]
    });
    component.onSubmit();
    expect(mockService.postSimpleRule).toHaveBeenCalledWith(
      'Rule 1',
      1,
      'AMEP',
      2023,
      'Quadrimester2',
      'LDIM-Student_tasks',
      1,
      'Test Message',
      true,
      'ValueGreaterThan',
      [5],
      10,
      'Individual'
    );
  });

  it('should call postDateRule with correct parameters on date rule submission', () => {
    fixture.detectChanges();
    const startDate = new Date(2023, 11, 1);
    const endDate = new Date(2023, 11, 15);
    component.form.setValue({
      game: { subjectAcronym: 'AMEP', course: 2023, period: 'Quadrimester2' },
      evaluableAction: { id: 'LDIM-Student_tasks', assessmentLevel: 'Individual' },
      achievement: 1,
      ruleType: 'date',
      startDate: startDate,
      endDate: endDate,
      name: 'Rule 1',
      repetitions: 1,
      achievementAssignmentMessage: 'Test Message',
      onlyFirstTime: true,
      achievementAssignmentUnits: 10,
      achievementAssignmentCondition: 'ValueGreaterThan',
      achievementAssignmentParameters: [5]
    });
    component.onSubmit();
    expect(mockService.postDateRule).toHaveBeenCalledWith(
      'Rule 1',
      1,
      'AMEP',
      2023,
      'Quadrimester2',
      'LDIM-Student_tasks',
      1,
      'Test Message',
      true,
      'ValueGreaterThan',
      [5],
      10,
      'Individual',
      '2023-12-01',
      '2023-12-15'
    );
  });
});
