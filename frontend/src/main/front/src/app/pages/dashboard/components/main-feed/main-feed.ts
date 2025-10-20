import { Component, EventEmitter, Input, Output } from '@angular/core';
import type { Member, Message }                   from '../../dashboard';
import { ChannelSummaryDto }                      from '../../../../shared/models/channel.model';
import { ButtonModule }                           from 'primeng/button';
import { LeaveDialogComponent }                   from '../dialogs/leave-dialog/leave-dialog';
import { MessageComposerComponent }               from '../message-composer/message-composer';
import { MessageListComponent }                   from '../message-list/message-list';


@Component({
  selector: 'app-dashboard-main',
  standalone: true,
  templateUrl: './main-feed.html',
  imports: [ButtonModule, LeaveDialogComponent, MessageComposerComponent, MessageListComponent]
})
export class DashboardMain {
  @Input() channel?: ChannelSummaryDto;
  @Input() members: Member[] = [];
  @Input() currentUserId = "1";
  @Input() messages: Message[] = [];
  @Input() myVotes: Record<number, number> = {};
  @Output() leaveChannel = new EventEmitter<ChannelSummaryDto>();
  @Output() sendMessage = new EventEmitter<{ text: string; attributedTo: Member; date: Date }>();
  @Output() deleteMessage = new EventEmitter<number>();
  @Output() likeMessage = new EventEmitter<number>();
  @Output() dislikeMessage = new EventEmitter<number>();

  showLeaveDialog = false;

  // Stateless: messages come from parent (Dashboard)

  onSendMessage(payload: { text: string; attributedTo: Member, date: Date }) {
    this.sendMessage.emit(payload);
  }

  onDeleteMessage(messageId: number) {
    this.deleteMessage.emit(messageId);
  }

  onLeaveClick() {
    if (!this.channel) return;
    this.showLeaveDialog = true;
  }

  onLike(messageId: number) {
    this.likeMessage.emit(messageId);
  }

  onDislike(messageId: number) {
    this.dislikeMessage.emit(messageId);
  }

  confirmLeave() {
    if (this.channel) {
      this.leaveChannel.emit(this.channel);
    }
    this.showLeaveDialog = false;
  }

  cancelLeave() {
    this.showLeaveDialog = false;
  }
}
