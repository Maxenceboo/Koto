import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import type { Message } from '../../dashboard';

@Component({
  selector: 'app-message-list',
  standalone: true,
  templateUrl: './message-list.html',
  imports: [DatePipe, ButtonModule]
})
export class MessageListComponent {
  @Input() messages: Message[] = [];
  @Input() currentUserId = 0;
  @Output() delete = new EventEmitter<number>();
}
