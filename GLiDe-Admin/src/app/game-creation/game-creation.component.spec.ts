import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameCreationComponent } from './game-creation.component';
import {GamificationEngineService} from '../services/gamification-engine.service';
import {HttpResponse, provideHttpClient} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {of} from 'rxjs';

describe('GameCreationComponent', () => {
  let component: GameCreationComponent;
  let fixture: ComponentFixture<GameCreationComponent>;
  let mockService: jasmine.SpyObj<GamificationEngineService>;

  beforeEach(async () => {
    mockService = jasmine.createSpyObj('GamificationEngineService', [
      'getGames',
      'getSubjects',
      'postSubject',
      'postGame',
      'postSimpleRule',
      'postDateRule',
      'postGameGroup',
    ]);

    mockService.getGames.and.returnValue(of([{
      subjectAcronym: 'AMEP',
      course: 2023,
      period: 'Quadrimester2',
      state: 'Active'
    }]));
    mockService.getSubjects.and.returnValue(of([{
      acronym: 'AMEP',
      code: 340379,
      name: "Ampliació a l'enginyeria del programari",
      school: 'UPC',
      studies: 'GEI'
    }]));

    const mockCreated = new HttpResponse({
      status: 201,
      body: {}
    });
    mockService.postDateRule.and.returnValue(of(mockCreated));
    mockService.postSimpleRule.and.returnValue(of(mockCreated));
    mockService.postSubject.and.returnValue(of(mockCreated))
    mockService.postGame.and.returnValue(of(mockCreated));
    mockService.postGameGroup.and.returnValue(of(mockCreated));

    await TestBed.configureTestingModule({
      imports: [GameCreationComponent],
      providers: [provideHttpClient(), provideAnimationsAsync(), { provide: GamificationEngineService, useValue: mockService }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GameCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize forms correctly', () => {
    expect(component.subjectForm).toBeDefined();
    expect(component.existingSubjectForm).toBeDefined();
    expect(component.gameForm).toBeDefined();
    expect(component.gameLevelPolicyForm).toBeDefined();
  });

  it('should fetch games and subjects on initialization', () => {
    mockService.getGames.and.returnValue(of([]));
    mockService.getSubjects.and.returnValue(of([]));
    component.ngOnInit();
    expect(mockService.getGames).toHaveBeenCalled();
    expect(mockService.getSubjects).toHaveBeenCalled();
    expect(component.games).toEqual([]);
    expect(component.existingSubjects).toEqual([]);
  });

  it('should handle form validation for new subjects', () => {
    component.subjectType = 'new';
    component.subjectForm.setValue({
      acronym: 'ASW',
      name: 'Aplicacions i Serveis Web',
      code: '101101',
      school: 'UPC',
      studies: 'GEI',
    });
    component.gameForm.setValue({
      subject_acronym: 'ASW',
      course: '2024',
      period: 'Quadrimester2',
      start_date: new Date(),
      end_date: new Date(),
      valid: true,
    });
    component.gameLevelPolicyForm.setValue({ a: 1, b: 1.4, c: 2 });

    expect(component.isFormValid()).toBe(true);
  });

  it('should handle form validation for existing subjects', () => {
    component.subjectType = 'existing';
    component.existingSubjectForm.setValue({ subject: { acronym: 'AMEP' } });
    component.gameForm.setValue({
      subject_acronym: 'AMEP',
      course: '2024',
      period: 'Quadrimester2',
      start_date: new Date(),
      end_date: new Date(),
      valid: true,
    });
    component.gameLevelPolicyForm.setValue({ a: 1, b: 1.4, c: 2 });

    expect(component.isFormValid()).toBe(true);
  });

  it('should handle game submission with new subject', () => {
    spyOn(component, 'isFormValid').and.returnValue(true);
    component.subjectType = 'new';
    component.gameForm.setValue({
      subject_acronym: 'ASW',
      course: '2024',
      period: 'Quadrimester2',
      start_date: new Date('Sun Dec 01 2024 00:00:00 GMT+0100 (hora estándar de Europa central)'),
      end_date: new Date('Sun Dec 01 2024 00:00:00 GMT+0100 (hora estándar de Europa central)'),
      valid: true,
    });
    component.submit();

    expect(mockService.postSubject).toHaveBeenCalled();
    expect(mockService.postGame).toHaveBeenCalled();
    expect(mockService.postGameGroup).toHaveBeenCalled();
  });
});
