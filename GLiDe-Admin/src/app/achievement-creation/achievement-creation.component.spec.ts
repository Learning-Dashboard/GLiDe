import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AchievementCreationComponent} from './achievement-creation.component';
import {HttpResponse, provideHttpClient} from '@angular/common/http';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {By} from '@angular/platform-browser';
import {GamificationEngineService} from '../services/gamification-engine.service';
import {of} from 'rxjs';

describe('AchievementCreationComponent', () => {
  let component: AchievementCreationComponent;
  let fixture: ComponentFixture<AchievementCreationComponent>;
  let mockService: jasmine.SpyObj<GamificationEngineService>;

  beforeEach(async () => {
    mockService = jasmine.createSpyObj('GamificationEngineService', ['postAchievement']);
    await TestBed.configureTestingModule({
      imports: [AchievementCreationComponent],
      providers: [provideHttpClient(), provideAnimationsAsync(),{ provide: GamificationEngineService, useValue: mockService }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AchievementCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty fields', () => {
    expect(component.form.get('name')?.value).toBeNull();
    expect(component.form.get('category')?.value).toBeNull();
  });

  it('should mark the form invalid when required fields are missing', () => {
    component.form.setValue({
      name: '',
      category: '',
    });
    expect(component.form.invalid).toBeTrue();
  });

  it('should mark the form valid when required fields are present', () => {
    component.form.setValue({
      name: 'Sample Achievement',
      category: 'Points',
    });
    expect(component.form.valid).toBeTrue();
  });

  it('should generate preview image', () => {
    component.selectedFile = new File(['content'], 'sample.png', {type: 'image/png'});
    component.imageUrl = 'data:image/png;base64,sampledata';

    fixture.detectChanges();

    const imgElement = fixture.debugElement.query(By.css('.preview-image'));
    expect(imgElement).toBeTruthy();
    expect(imgElement.nativeElement.src).toContain('data:image/png;base64,sampledata');
  });

  it('should clear the selected file and preview when delete is clicked', () => {
    const fileInput = fixture.debugElement.query(By.css('#file')).nativeElement;

    component.imageUrl = 'data:image/png;base64,sampledata';
    component.selectedFile = new File(['content'], 'sample.png', { type: 'image/png' });

    component.deleteFile(fileInput);

    expect(component.imageUrl).toBeNull();
    expect(component.selectedFile).toBeNull();
    expect(fileInput.value).toBe('');
  });

  it('should call the service on form submission', () => {
    const mockFile = new File(['content'], 'sample.png', { type: 'image/png' });
    const mockResponse = new HttpResponse<Object>({
      status: 201
    });
    mockService.postAchievement.and.returnValue(of(mockResponse));

    component.form.setValue({
      name: 'Sample Achievement',
      category: 'Points',
    });
    component.selectedFile = mockFile;

    const fileInput = fixture.debugElement.query(By.css('#file')).nativeElement;
    component.onSubmit(fileInput);

    expect(mockService.postAchievement).toHaveBeenCalledWith(
      'Sample Achievement',
      mockFile,
      'Points'
    );
    expect(component.form.get('name')?.value).toBeNull();
    expect(component.form.get('category')?.value).toBeNull();
    expect(component.imageUrl).toBeNull();
    expect(component.selectedFile).toBeNull();
  });

  it('should disable the submit button when the form is invalid', () => {
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
    expect(submitButton.disabled).toBeTrue();

    component.form.setValue({
      name: 'Sample Achievement',
      category: 'Points',
    });
    fixture.detectChanges();

    expect(submitButton.disabled).toBeFalse();
  });
});
