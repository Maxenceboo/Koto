import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardLeft } from './components/left-side/left-side';
import { DashboardMain } from './components/main-feed/main-feed';
import { DashboardRight } from './components/right-side/right-side';

export interface Channel {
  id: number;
  name: string;
}

export interface Member {
  id: number;
  name: string;
}

export interface Message {
  id: number;
  text: string;
  authorId: number;
  createdAt: Date;
  likes: number;
  attributedTo: Member;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.html',
  imports: [CommonModule, DashboardLeft, DashboardMain, DashboardRight]
})
export class DashboardPage {
  // TODO: remplacer par un appel au backend
  // Mock channels list (à relier au backend plus tard)
  // Quelques channels par défaut
  channels: Channel[] = [
    { id: 1, name: 'Général' },
    { id: 2, name: 'Développement' },
    { id: 3, name: 'Design' },
    { id: 4, name: 'Support' },
    { id: 5, name: 'Random' }
    ];

  // TODO: remplacer par un appel au backend
  // Channel sélectionné
  selectedChannel: Channel | undefined = this.channels[0];
  // TODO: remplacer par un appel au backend
  // Mock current user id
  currentUserId = 1;

  // TODO: remplacer par un appel au backend
  // Mock members list (à relier au backend plus tard)
  members: Member[] = [
    { id: 1, name: 'Alice' },
    { id: 2, name: 'Bob' },
    { id: 3, name: 'Charlie' },
    { id: 4, name: 'Diane' }
  ];

  // TODO: remplacer par un appel au backend
  // Membership par channel (tous les membres n'ont pas tous les channels)
  private channelMembers: Record<number, Member[]> = {
    1: [ { id: 1, name: 'Alice' }, { id: 2, name: 'Bob' } ],
    2: [ { id: 2, name: 'Bob' }, { id: 3, name: 'Charlie' } ],
    3: [ { id: 1, name: 'Alice' }, { id: 4, name: 'Diane' } ],
    4: [ { id: 3, name: 'Charlie' } ],
    5: [ { id: 4, name: 'Diane' } ]
  };

  // Membre du channel sélectionné
  get selectedMembers(): Member[] {
    if (!this.selectedChannel) return [];
    return this.channelMembers[this.selectedChannel.id] ?? [];
  }

  // TODO: remplacer par un appel au backend
  // Messages par channel et séquence locale d'ID de message
  private messagesStore = new Map<number, Message[]>();
  private nextMessageId = 1;

  // Initialisation de quelques messages par défaut
  // Messages du channel sélectionné
  get selectedMessages(): Message[] {
    if (!this.selectedChannel) return [];
    return this.messagesStore.get(this.selectedChannel.id) ?? [];
  }

  onChannelSelect(channel: Channel) {
    this.selectedChannel = channel;
  }


  onJoinByUrl(url: string) {
    // TODO integrate with backend invitation logic
    // For now, add a placeholder joined channel using the URL tail as name
    const tail = url.split('/').filter(Boolean).pop() ?? 'channel';
    const id = (this.channels.at(-1)?.id ?? 0) + 1;
    const newChannel: Channel = { id, name: tail };
    this.channels = [...this.channels, newChannel];
    this.selectedChannel = newChannel;
    // Nouveau channel: pas de membres par défaut
    this.channelMembers[id] = [];
    this.messagesStore.set(id, []);
  }

  onCreateChannel(payload: { name: string }) {
    // TODO integrate with backend creation logic
    const id = (this.channels.at(-1)?.id ?? 0) + 1;
    const newChannel: Channel = { id, name: payload.name };
    this.channels = [...this.channels, newChannel];
    this.selectedChannel = newChannel;
    // Nouveau channel: pas de membres par défaut
    this.channelMembers[id] = [];
    this.messagesStore.set(id, []);
  }

  onLeaveChannel(channel: Channel) {
    // Remove the channel from the list
    this.channels = this.channels.filter(c => c.id !== channel.id);

    // If the removed channel was selected, pick another or clear selection
    if (this.selectedChannel?.id === channel.id) {
      this.selectedChannel = this.channels[0];
    }
    // Nettoyer la liste des membres du channel supprimé
    delete this.channelMembers[channel.id];
    this.messagesStore.delete(channel.id);
  }

  onCreateMessage(payload: { text: string; attributedTo: Member }) {
    if (!this.selectedChannel) return;
    const list = [...(this.messagesStore.get(this.selectedChannel.id) ?? [])];
    const id = this.nextMessageId++;
    list.push({
      id,
      text: payload.text,
      authorId: this.currentUserId,
      createdAt: new Date(),
      likes: 0,
      attributedTo: payload.attributedTo
    });
    this.messagesStore.set(this.selectedChannel.id, list);
  }

  onDeleteMessage(messageId: number) {
    if (!this.selectedChannel) return;
    const list = this.messagesStore.get(this.selectedChannel.id) ?? [];
    this.messagesStore.set(
      this.selectedChannel.id,
      list.filter(m => m.id !== messageId)
    );
  }
}
