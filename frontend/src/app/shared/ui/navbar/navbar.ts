import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.html',
  imports: [RouterModule, MenubarModule, TieredMenuModule]
})
export class NavbarComponent {
  // TODO : remplace plus tard par ton AuthService
  isAuth = true;
  notifCount = 2;

  userMenuItems: MenuItem[] = [];

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
