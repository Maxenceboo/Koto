import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { PopoverModule } from 'primeng/popover';
import { MenuItem } from 'primeng/api';
import { UserStore } from '../../store/user.store';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.html',
  imports: [RouterModule, MenubarModule, TieredMenuModule, PopoverModule]
})
export class NavbarComponent {


  notifCount = 2;
  userMenuItems: MenuItem[] = [];
  notifications: { id: number; text: string; date: Date; read?: boolean }[] = [
    { id: 1, text: 'Alice a cité Bob', date: new Date() },
    { id: 2, text: 'Nouvelle invite au channel Design', date: new Date() }
  ];
  constructor(private router: Router, private userStore: UserStore) {}

  get isAuth(): boolean {
    return this.userStore.isLoggedIn();
  }

  ngOnInit() {
    this.userMenuItems = [
      { label: 'Profile',  command: () => this.router.navigate(['/profile']) },
      { label: 'Settings', command: () => this.router.navigate(['/settings']) },
      { separator: true },
      { label: 'Logout',   command: () => { this.userStore.logout(); this.router.navigate(['/login']); } }
    ];
  }
}
