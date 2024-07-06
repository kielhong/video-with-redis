package com.example.mytv.domain.channel;

import com.example.mytv.adapter.out.redis.channel.ChannelRedisHash;
import com.example.mytv.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Channel {
    private String id;
    private ChannelSnippet snippet;
    private ChannelStatistics statistics;
    private User contentOwner;

    public static Channel from(ChannelRedisHash channel) {
        return Channel.builder()
                .id(channel.getId())
                .build();
    }
}