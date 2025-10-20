import { Injectable } from '@angular/core';
import { HttpClient }                       from '@angular/common/http';
import { ChannelSummaryDto, CreateChannel } from '../models/channel.model';

type ChannelResponse = {}

@Injectable({ providedIn: 'root' })
class ChannelService {
  constructor(private http: HttpClient) {}

  createChannel (createchannel: CreateChannel) {
    return this.http.post<ChannelResponse>('/api/channels/', createchannel);
  }

  getChannel(userId: string){
    return this.http.get<ChannelSummaryDto[]>(`/api/channels/${userId}`);
  }
}

export default ChannelService;
