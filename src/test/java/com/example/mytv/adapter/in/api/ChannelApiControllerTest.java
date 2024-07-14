package com.example.mytv.adapter.in.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mytv.adapter.in.api.dto.ChannelRequest;
import com.example.mytv.adapter.in.api.dto.ChannelSnippetRequest;
import com.example.mytv.application.port.in.ChannelUseCase;
import com.example.mytv.domain.channel.ChannelFixtures;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelApiController.class)
class ChannelApiControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChannelUseCase channelUseCase;

    @Test
    @DisplayName("POST /api/v1/channels")
    void testCreateChannel() throws Exception {
        // Given
        var channelRequest = new ChannelRequest(new ChannelSnippetRequest("title", "description", "https://example.com/thumbnail.jpg"), "userId");
        given(channelUseCase.createChannel(any())).willReturn(ChannelFixtures.stub("channelId"));

        // When
        mockMvc
            .perform(
                post("/api/v1/channels")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(channelRequest))
            )
            .andExpectAll(
                status().isOk()
            );

        verify(channelUseCase).createChannel(
            argThat(param -> {
                    then(param.getSnippet())
                        .hasFieldOrPropertyWithValue("title", "title")
                        .hasFieldOrPropertyWithValue("description", "description")
                        .hasFieldOrPropertyWithValue("thumbnailUrl", "https://example.com/thumbnail.jpg");
                    then(param.getContentOwnerId())
                        .isEqualTo("userId");
                    return true;
                })
        );
    }

    @Test
    @DisplayName("PUT /api/v1/channels")
    void testUpdateChannel() throws Exception {
        // Given
        var channelRequest = new ChannelRequest(new ChannelSnippetRequest("title2", "description2", "https://example.com/thumbnail2.jpg"), "userId");
        given(channelUseCase.updateChannel(any(), any())).willReturn(ChannelFixtures.stub("channelId"));

        // When
        mockMvc
            .perform(
                put("/api/v1/channels/{id}", "channelId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(channelRequest))
            )
            .andExpectAll(
                status().isOk()
            );

        // Then
        verify(channelUseCase).updateChannel(
            eq("channelId"),
            argThat(param -> {
                then(param.getSnippet())
                    .hasFieldOrPropertyWithValue("title", "title2")
                    .hasFieldOrPropertyWithValue("description", "description2")
                    .hasFieldOrPropertyWithValue("thumbnailUrl", "https://example.com/thumbnail2.jpg");
                then(param.getContentOwnerId())
                    .isEqualTo("userId");
                return true;
            })
        );
    }

    @Nested
    @DisplayName("GET /api/v1/channels/{channelId}")
    class GetChannel {
        @Test
        @DisplayName("Channel 반환하고 200 OK")
        void returnChannel() throws Exception {
            // Given
            var channel = ChannelFixtures.stub("channelId");
            given(channelUseCase.getChannel("channelId")).willReturn(channel);
            // When
            mockMvc
                .perform(
                    get("/api/v1/channels/{channelId}", "channelId")
                )
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$.id").value("channelId"),
                    jsonPath("$.snippet.title").value(channel.getSnippet().getTitle()),
                    jsonPath("$.statistics.videoCount").value(channel.getStatistics().getVideoCount()),
                    jsonPath("$.contentOwner.id").value(channel.getContentOwner().getId())
                );
        }

        @Test
        @DisplayName("Channel 없으면 404 NotFound")
        void notFound() throws Exception {
            // Given
            given(channelUseCase.getChannel("channelId")).willThrow(new NoSuchElementException());
            mockMvc
                .perform(get("/api/v1/channels/{channelId}", "channelId"))
                .andExpect(
                    status().isNotFound()
                );
        }
    }
}