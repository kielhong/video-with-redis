package com.example.mytv.application;

import com.example.mytv.adapter.in.api.dto.ChannelRequest;
import com.example.mytv.application.port.in.ChannelUseCase;
import com.example.mytv.application.port.out.LoadChannelPort;
import com.example.mytv.application.port.out.LoadUserPort;
import com.example.mytv.application.port.out.SaveChannelPort;
import com.example.mytv.domain.channel.Channel;
import com.example.mytv.domain.channel.ChannelSnippet;
import com.example.mytv.domain.channel.ChannelStatistics;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelService implements ChannelUseCase {
    private final LoadChannelPort loadChannelPort;
    private final LoadUserPort loadUserPort;
    private final SaveChannelPort saveChannelPort;

    @Override
    public Channel createChannel(ChannelRequest request) {
        var contentOwner = loadUserPort.loadUser(request.getContentOwnerId()).get();
        var channel = Channel.builder()
            .id(UUID.randomUUID().toString())
            .snippet(
                ChannelSnippet.builder()
                    .title(request.getSnippet().getTitle())
                    .description(request.getSnippet().getDescription())
                    .thumbnailUrl(request.getSnippet().getThumbnailUrl())
                    .publishedAt(LocalDateTime.now())
                    .build()
            )
            .statistics(
                ChannelStatistics.builder()
                    .viewCount(0)
                    .subscriberCount(0)
                    .videoCount(0)
                    .commentCount(0)
                    .build()
            )
            .contentOwner(contentOwner)
            .build();

        saveChannelPort.saveChannel(channel);
        return channel;
    }

    @Override
    public Channel updateChannel(String channelId, ChannelRequest channelRequest) {
        var channel = loadChannelPort.loadChannel(channelId).get();
        channel.updateSnippet(channelRequest.getSnippet());

        saveChannelPort.saveChannel(channel);
        return channel;
    }

    @Override
    public Channel getChannel(String id) {
        return loadChannelPort.loadChannel(id).get();
    }
}
