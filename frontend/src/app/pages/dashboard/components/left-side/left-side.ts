import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ListboxModule } from 'primeng/listbox';
import type { Channel } from '../../dashboard';
import { ButtonModule } from 'primeng/button';
import { AddChannelDialogComponent } from '../dialogs/add-channel-dialog/add-channel-dialog';

@Component({
  selector: 'app-dashboard-left',
  standalone: true,
  templateUrl: './left-side.html',
  imports: [FormsModule, ListboxModule, ButtonModule, AddChannelDialogComponent]
})
export class DashboardLeft {
  @Input() channels: Channel[] = [];
  @Input() selectedChannel?: Channel;
  @Output() channelSelect = new EventEmitter<Channel>();
  @Output() joinChannelByUrl = new EventEmitter<string>();
  @Output() createChannel = new EventEmitter<{ name: string }>();

  internalSelected?: Channel;
  showAddDialog = false;

  ngOnChanges(): void {
    this.internalSelected = this.selectedChannel;
  }

  emitSelection() {
    if (this.internalSelected) {
      this.channelSelect.emit(this.internalSelected);
    }
  }

  onAddClick() {
    this.showAddDialog = true;
  }

  onJoinByUrl(url: string) {
    this.joinChannelByUrl.emit(url);
    this.showAddDialog = false;
  }

  onCreateChannel(payload: { name: string }) {
    this.createChannel.emit(payload);
    this.showAddDialog = false;
  }
}
