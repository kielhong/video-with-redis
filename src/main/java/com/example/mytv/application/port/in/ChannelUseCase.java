package com.example.mytv.application.port.in;

import com.example.mytv.adapter.in.api.ChannelRequest;
import com.example.mytv.domain.channel.Channel;

public interface ChannelUseCase {
    Channel createChannel(ChannelRequest channelRequest);
    Channel getChannel(String id);
}