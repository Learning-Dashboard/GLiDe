import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TeacherCreationComponent } from './teacher-creation.component';
import { TeacherService } from '../services/teachers.service';

describe('TeacherCreationComponent', () => {
  let component: TeacherCreationComponent;
  let fixture: ComponentFixture<TeacherCreationComponent>;
  let mockRouter = { navigate: jasmine.createSpy('navigate') };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TeacherCreationComponent],
      imports: [ReactiveFormsModule, HttpClientTestingModule],
      providers: [
        TeacherService,
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TeacherCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});