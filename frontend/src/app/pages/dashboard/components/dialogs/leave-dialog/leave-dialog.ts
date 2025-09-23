import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import type { Channel } from '../../../dashboard';

@Component({
  selector: 'app-leave-dialog',
  standalone: true,
  templateUrl: './leave-dialog.html',
  imports: [DialogModule, ButtonModule]
})
export class LeaveDialogComponent {
  @Input() visible = false;
  @Input() channel?: Channel;
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
}

