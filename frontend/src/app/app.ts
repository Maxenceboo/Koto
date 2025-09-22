import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {NavbarComponent} from './shared/ui/navbar/navbar';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.html',
  imports: [RouterOutlet, NavbarComponent],
})
export class App {
  protected readonly title = signal('koto-web');
}
