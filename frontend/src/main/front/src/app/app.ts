import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {NavbarComponent} from './shared/ui/navbar/navbar';
import {FooterComponent} from './shared/ui/footer/footer';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.html',
  imports: [RouterOutlet, NavbarComponent, FooterComponent],
})
export class App {
  protected readonly title = signal('koto-web');
}
