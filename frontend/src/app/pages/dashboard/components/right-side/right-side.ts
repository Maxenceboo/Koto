import { Component, Input } from '@angular/core';
import type { Member } from '../../dashboard';

@Component({
  selector: 'app-dashboard-right',
  standalone: true,
  templateUrl: './right-side.html'
})
export class DashboardRight {
  @Input() members: Member[] = [];
}
