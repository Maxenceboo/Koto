import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DialogModule }                           from 'primeng/dialog';
import { ButtonModule }                           from 'primeng/button';
import type { ChannelSummaryDto }                 from '../../../../../shared/models/channel.model';

@Component({
  selector: 'app-leave-dialog',
  standalone: true,
  templateUrl: './leave-dialog.html',
  imports: [DialogModule, ButtonModule]
})
export class LeaveDialogComponent {
  @Input() visible = false;
  @Input() channel?: ChannelSummaryDto;
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
}

