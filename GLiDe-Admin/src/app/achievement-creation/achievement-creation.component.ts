import { Component } from '@angular/core';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {NgIf} from '@angular/common';
import {GamificationEngineService} from '../services/gamification-engine.service';

@Component({
  selector: 'app-achievement-creation',
  imports: [MatFormFieldModule, MatInputModule, MatSelectModule, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, NgIf],
  templateUrl: './achievement-creation.component.html',
  standalone: true,
  styleUrl: './achievement-creation.component.css'
})
export class AchievementCreationComponent {
  imageUrl: any;
  selectedFile: any;

  constructor(private service: GamificationEngineService) {}

  form: FormGroup = new FormGroup({
    name: new FormControl(),
    category: new FormControl(),
  });

  onFileSelected(event: Event){
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0){
      const file = input.files![0];
      if (file.type.startsWith('image/')){
        this.selectedFile = file;
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.imageUrl = e.target.result;
        }
        reader.readAsDataURL(file);
      }
    }
  }

  deleteFile(inputElement: HTMLInputElement){
    this.imageUrl = null;
    this.selectedFile = null;
    inputElement.value='';
  }

  onSubmit(inputElement?: HTMLInputElement){
    this.service.postAchievement(this.form.get('name')?.value, this.selectedFile, this.form.get('category')?.value).subscribe({
      next: (result) => {
        console.log(result.status);
        alert('Achievement created successfully.');
        this.form.reset();
        if (inputElement) this.deleteFile(inputElement);
      },
      error: () => alert('An unexpected error occurred.')
    });
  }
}
