import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-channel-dialog',
  standalone: true,
  templateUrl: './add-channel-dialog.html',
  imports: [DialogModule, ButtonModule, InputTextModule, SelectButtonModule, FormsModule]
})
export class AddChannelDialogComponent {
  @Input() visible = false;
  @Output() visibleChange = new EventEmitter<boolean>();
  @Output() cancel = new EventEmitter<void>();
  @Output() joinByUrl = new EventEmitter<string>();
  @Output() create = new EventEmitter<{ name: string }>();

  method: 'url' | 'create' = 'url';
  methodOptions = [
    { label: 'By URL', value: 'url' },
    { label: 'Create', value: 'create' }
  ];
  url = '';
  name = '';

  confirmAction() {
    if (this.method === 'url' && this.url.trim()) {
      this.joinByUrl.emit(this.url.trim());
      this.reset();
      return;
    }
    if (this.method === 'create' && this.name.trim()) {
      this.create.emit({ name: this.name.trim() });
      this.reset();
    }
  }

  private reset() {
    this.url = '';
    this.name = '';
    this.visible = false;
    this.visibleChange.emit(false);
  }
}

