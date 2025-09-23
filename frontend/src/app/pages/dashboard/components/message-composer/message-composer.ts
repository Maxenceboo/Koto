import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AutoCompleteModule } from 'primeng/autocomplete';
import type { Member } from '../../dashboard';

@Component({
  selector: 'app-message-composer',
  standalone: true,
  templateUrl: './message-composer.html',
  imports: [FormsModule, ButtonModule, InputTextModule, AutoCompleteModule]
})
export class MessageComposerComponent {
  @Input() members: Member[] = [];
  @Output() send = new EventEmitter<{ text: string; attributedTo: Member }>();
  text = '';
  selected?: Member;
  filtered: Member[] = [];

  submit() {
    const value = this.text.trim();
    if (!value || !this.selected) return;
    this.send.emit({ text: value, attributedTo: this.selected });
    this.text = '';
    this.selected = undefined;
  }

  search(event: { query: string }) {
    const q = (event?.query ?? '').toLowerCase();
    this.filtered = this.members.filter(m => m.name.toLowerCase().includes(q));
  }
}
