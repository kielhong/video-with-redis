package com.example.mytv.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import com.example.mytv.adapter.in.api.dto.ChannelRequest;
import com.example.mytv.adapter.in.api.dto.ChannelSnippetRequest;
import com.example.mytv.application.port.out.LoadChannelPort;
import com.example.mytv.application.port.out.LoadUserPort;
import com.example.mytv.application.port.out.SaveChannelPort;
import com.example.mytv.domain.channel.ChannelFixtures;
import com.example.mytv.domain.user.UserFixtures;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {
    private ChannelService sut;
    @Mock
    private LoadChannelPort loadChannelPort;
    @Mock
    private LoadUserPort loadUserPort;
    @Mock
    private SaveChannelPort saveChannelPort;

    @BeforeEach
    void setUp() {
        sut = new ChannelService(loadChannelPort, loadUserPort, saveChannelPort);
    }

    @Test
    @DisplayName("createChannel")
    void testCreateChannel() {
        // Given
        var channelRequest = new ChannelRequest(new ChannelSnippetRequest("title", "description", "https://example.com/thumbnail.jpg"), "userId");
        willDoNothing().given(saveChannelPort).saveChannel(any());
        given(loadUserPort.loadUser(any())).willReturn(Optional.of(UserFixtures.stub()));
        // When
        var result = sut.createChannel(channelRequest);
        // Then
        then(result)
            .isNotNull()
            .hasFieldOrProperty("id")
            .hasFieldOrPropertyWithValue("snippet.title", "title")
            .hasFieldOrPropertyWithValue("snippet.description", "description")
            .hasFieldOrPropertyWithValue("snippet.thumbnailUrl", "https://example.com/thumbnail.jpg")
            .hasFieldOrPropertyWithValue("statistics.viewCount", 0)
            .hasFieldOrPropertyWithValue("statistics.videoCount", 0)
            .hasFieldOrPropertyWithValue("statistics.commentCount", 0)
            .hasFieldOrPropertyWithValue("statistics.subscriberCount", 0)
            .hasFieldOrPropertyWithValue("contentOwner.id", "userId");
    }

    @Test
    @DisplayName("updateChannel")
    void testUpdateChannel() {
        // Given
        var channelId = "channelId";
        var channelRequest = new ChannelRequest(new ChannelSnippetRequest("title2", "description2", "https://example.com/thumbnail2.jpg"), "userId");
        given(loadChannelPort.loadChannel(any())).willReturn(Optional.of(ChannelFixtures.stub(channelId)));
        willDoNothing().given(saveChannelPort).saveChannel(any());
        // When
        var result = sut.updateChannel(channelId, channelRequest);
        // Then
        then(result)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", channelId)
            .hasFieldOrPropertyWithValue("snippet.title", "title2")
            .hasFieldOrPropertyWithValue("snippet.description", "description2")
            .hasFieldOrPropertyWithValue("snippet.thumbnailUrl", "https://example.com/thumbnail2.jpg")
            .hasFieldOrPropertyWithValue("contentOwner.id", "userId");
    }

    @Test
    @DisplayName("getChannel")
    void testGetChannel() {
        var id = "channelId";
        given(loadChannelPort.loadChannel(any()))
            .willReturn(Optional.of(ChannelFixtures.stub(id)));

        var result = sut.getChannel(id);

        then(result)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", id);
    }
}