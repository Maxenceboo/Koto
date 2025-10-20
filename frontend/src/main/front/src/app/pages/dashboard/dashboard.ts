import { Component, inject, OnInit, Signal } from '@angular/core';
import { CommonModule }                      from '@angular/common';
import { DashboardLeft } from './components/left-side/left-side';
import { DashboardMain } from './components/main-feed/main-feed';
import { DashboardRight } from './components/right-side/right-side';
import ChannelService from '../../shared/services/channel.service';
import { UserStore } from '../../shared/store/user.store'
import { ChannelSummaryDto, CreateChannel } from '../../shared/models/channel.model';


export interface Member {
  id: string;
  name: string;
}

export interface Message {
  id: string;
  text: string;
  authorId: number;
  createdAt: Date;
  likes: number;
  dislikes: number;
  attributedTo: Member;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  imports: [CommonModule, DashboardLeft, DashboardMain, DashboardRight]
})
export class DashboardPage implements OnInit {


  private readonly userStore = inject(UserStore);

  private chanelService = inject(ChannelService);

  public readonly currentUserId = this.userStore.state.id();

  public channels: ChannelSummaryDto[]= [];

  public selectedChannel: ChannelSummaryDto;


  ngOnInit(): void {

      this.chanelService.getChannel(this.currentUserId).subscribe(channels =>{
        this.channels = channels;
        this.selectedChannel = channels[0];
      } )

  }


  // TODO: remplacer par un appel au backend
  // Mock members list (à relier au backend plus tard)
  members: Member[] = [
    { id: '1', name: 'Alice' },
    { id: '2', name: 'Bob' },
    { id: '3', name: 'Charlie' },
    { id: '4', name: 'Diane' }
  ];

  // TODO: remplacer par un appel au backend
  // Membership par channel (tous les membres n'ont pas tous les channels)
  private channelMembers: Record<string, Member[]> = {
    '1': [ { id: '1', name: 'Alice' }, { id: '2', name: 'Bob' } ],
    '2': [ { id: '2', name: 'Bob' }, { id: '3', name: 'Charlie' } ],
    '3': [ { id: '1', name: 'Alice' }, { id: '4', name: 'Diane' } ],
    '4': [ { id: '3', name: 'Charlie' } ],
    '5': [ { id: '4', name: 'Diane' } ]
  };

  // Membre du channel sélectionné
  get selectedMembers(): Member[] {
    if (!this.selectedChannel) return [];
    return this.channelMembers[this.selectedChannel.id] ?? [];
  }

  // TODO: remplacer par un appel au backend
  // Messages par channel et séquence locale d'ID de message
  // private messagesStore = new Map<number, Message[]>();
  // private nextMessageId = 1;
  // private voteStore = new Map<number, Map<number, number>>();

  // Initialisation de quelques messages par défaut
  // // Messages du channel sélectionné
  // get selectedMessages(): Message[] {
    // if (!this.selectedChannel) return [];
    // return this.messagesStore.get(this.selectedChannel.id) ?? [];
  // }

  // get selectedVotesForMe(): Record<number, number> {
  //   const map: Record<number, number> = {};
  //   for (const m of this.selectedMessages) {
  //     const voters = this.voteStore.get(m.id);
  //     const v = voters?.get(this.currentUserId) ?? 0;
  //     if (v) map[m.id] = v;
  //   }
  //   return map;
  // }

  onChannelSelect(channel: ChannelSummaryDto) {
    this.selectedChannel = channel;
  }

  onJoinByUrl(url: string) {
  //   // TODO integrate with backend invitation logic
  //   // For now, add a placeholder joined channel using the URL tail as name
  //   const tail = url.split('/').filter(Boolean).pop() ?? 'channel';
  //   const id = (this.channels.at(-1)?.id ?? 0) + 1;
  //   const newChannel: Channel = { id, name: tail };
  //   this.channels = [...this.channels, newChannel];
  //   this.selectedChannel = newChannel;
  //   // Nouveau channel: pas de membres par défaut
  //   this.channelMembers[id] = [];
  //   this.messagesStore.set(id, []);
  }

  onCreateChannel(payload: { name: string }) {

    let createChannel : CreateChannel = {name : payload.name , createdBy : this.currentUserId}

    this.chanelService.createChannel(createChannel);

    this.refreshData();

  }

  onLeaveChannel(channel: ChannelSummaryDto) {
    // // Remove the channel from the list
    // this.channels = this.channels.filter(c => c.id !== channel.id);
    //
    // // If the removed channel was selected, pick another or clear selection
    // if (this.selectedChannel?.id === channel.id) {
    //   this.selectedChannel = this.channels[0];
    // }
    // // Nettoyer la liste des membres du channel supprimé
    // delete this.channelMembers[channel.id];
    // this.messagesStore.delete(channel.id);
  }

  onCreateMessage(payload: { text: string; attributedTo: Member, date: Date }) {
    // if (!this.selectedChannel) return;
    // const list = [...(this.messagesStore.get(this.selectedChannel.id) ?? [])];
    // const id = this.nextMessageId++;
    //  list.push({
    //   id,
    //   text: payload.text,
    //   authorId: this.currentUserId,
    //   createdAt: payload.date,
    //   likes: 0,
    //   dislikes: 0,
    //   attributedTo: payload.attributedTo
    // });
    // this.messagesStore.set(this.selectedChannel.id, list);
  }

  onDeleteMessage(messageId: number) {
  //   if (!this.selectedChannel) return;
  //   const list = this.messagesStore.get(this.selectedChannel.id) ?? [];
  //   this.messagesStore.set(
  //     this.selectedChannel.id,
  //     list.filter(m => m.id !== messageId)
  //   );
  }

  onLikeMessage(messageId: number) {
  //   this.applyVote(messageId, 1);
  // }
  //
  // onDislikeMessage(messageId: number) {
  //   this.applyVote(messageId, -1);
  }

  private applyVote(messageId: number, vote: 1 | -1) {
  //   if (!this.selectedChannel) return;
  //   const list = this.messagesStore.get(this.selectedChannel.id) ?? [];
  //   const idx = list.findIndex(m => m.id === messageId);
  //   if (idx === -1) return;
  //   const message = { ...list[idx] };
  //
  //   const voters = this.voteStore.get(messageId) ?? new Map<number, number>();
  //   const prev = voters.get(this.currentUserId) ?? 0;
  //
  //   // remove previous vote
  //   if (prev === 1) message.likes = Math.max(0, message.likes - 1);
  //   if (prev === -1) message.dislikes = Math.max(0, message.dislikes - 1);
  //
  //   // toggle logic: if clicking same vote, clear it; else set new vote
  //   const next = prev === vote ? 0 : vote;
  //   if (next === 1) message.likes += 1;
  //   if (next === -1) message.dislikes += 1;
  //
  //   // persist vote
  //   if (next === 0) voters.delete(this.currentUserId); else voters.set(this.currentUserId, next);
  //   this.voteStore.set(messageId, voters);
  //
  //   // persist message list immutably
  //   const newList = [...list];
  //   newList[idx] = message;
  //   this.messagesStore.set(this.selectedChannel.id, newList);
  }


  refreshData(): void {
    this.ngOnInit();
  }

}
