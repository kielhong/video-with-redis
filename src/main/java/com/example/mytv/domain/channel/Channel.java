package com.example.mytv.domain.channel;

import com.example.mytv.adapter.in.api.dto.ChannelSnippetRequest;
import com.example.mytv.adapter.out.redis.channel.ChannelRedisHash;
import com.example.mytv.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Channel {
    private String id;
    private ChannelSnippet snippet;
    private ChannelStatistics statistics;
    private User contentOwner;

    public static Channel from(ChannelRedisHash channelRedisHash) {
        return Channel.builder()
            .id(channelRedisHash.getId())
            .snippet(channelRedisHash.getSnippet().toDomain())
            .statistics(channelRedisHash.getStatistics().toDomain())
            .contentOwner(channelRedisHash.getContentOwner().toDomain())
            .build();
    }

    public void updateSnippet(ChannelSnippetRequest snippetRequest) {
        this.snippet = ChannelSnippet.builder()
            .title(snippetRequest.getTitle())
            .description(snippetRequest.getDescription())
            .thumbnailUrl(snippetRequest.getThumbnailUrl())
            .build();
    }
}
