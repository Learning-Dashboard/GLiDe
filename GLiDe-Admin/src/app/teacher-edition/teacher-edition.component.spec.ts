import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { TeacherEditionComponent } from './teacher-edition.component';
import { TeacherService } from '../services/teachers.service';

describe('TeacherEditionComponent', () => {
  let component: TeacherEditionComponent;
  let fixture: ComponentFixture<TeacherEditionComponent>;
  let mockRouter = { navigate: jasmine.createSpy('navigate') };
  let mockActivatedRoute = {
    snapshot: { paramMap: { get: () => 'test@example.com' } }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TeacherEditionComponent],
      imports: [ReactiveFormsModule, HttpClientTestingModule],
      providers: [
        TeacherService,
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TeacherEditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});