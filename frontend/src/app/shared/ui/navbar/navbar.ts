import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { PopoverModule } from 'primeng/popover';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.html',
  imports: [RouterModule, MenubarModule, TieredMenuModule, PopoverModule]
})
export class NavbarComponent {
  // TODO : remplace plus tard par ton AuthService
  isAuth = true;
  notifCount = 2;
  userMenuItems: MenuItem[] = [];
  notifications: { id: number; text: string; date: Date; read?: boolean }[] = [
    { id: 1, text: 'Alice a cité Bob', date: new Date() },
    { id: 2, text: 'Nouvelle invite au channel Design', date: new Date() }
  ];
  constructor(private router: Router) {}


  ngOnInit() {
    this.userMenuItems = [
      { label: 'Profile',  command: () => this.router.navigate(['/profile']) },
      { label: 'Settings', command: () => this.router.navigate(['/settings']) },
      { separator: true },
      { label: 'Logout',   command: () => { this.isAuth = false; this.router.navigate(['/login']); } }
    ];
  }
}
