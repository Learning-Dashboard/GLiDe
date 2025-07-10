import { TestBed } from '@angular/core/testing';

import { GamificationEngineService } from './gamification-engine.service';
import {provideHttpClient} from '@angular/common/http';

describe('GamificationEngineService', () => {
  let service: GamificationEngineService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [provideHttpClient()]
    }).compileComponents();

    TestBed.configureTestingModule({});
    service = TestBed.inject(GamificationEngineService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
