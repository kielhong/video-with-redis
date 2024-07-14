package com.example.mytv.adapter.in.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mytv.application.port.in.VideoUseCase;
import com.example.mytv.domain.video.VideoFixtures;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VideoApiController.class)
class VideoApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoUseCase videoUseCase;

    @Nested
    @DisplayName("GET /api/v1/videos/{videoId}")
    class GetVideo {
        @Test
        @DisplayName("vidoe 가 있으면 200 OK")
        void testGetVideo() throws Exception {
            // Given
            var videoId = "videoId";
            given(videoUseCase.getVideo(any())).willReturn(VideoFixtures.stub(videoId));

            // When
            mockMvc
                .perform(
                    get("/api/v1/videos/{videoId}", videoId)
                )
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$.id").value(videoId)
                );
        }
    }

    @Nested
    @DisplayName("GET /api/v1/videos?channelId={channelId}")
    class ListVideo {
        @Test
        @DisplayName("200 OK, 목록 반환")
        void testListVideo() throws Exception {
            // Given
            var channelId = "channelId";
            var list = LongStream.range(1L, 4L)
                .mapToObj(i -> VideoFixtures.stub("videoId" + i))
                .toList();
            given(videoUseCase.listVideos(any())).willReturn(list);

            // When
            mockMvc
                .perform(
                    get("/api/v1/videos?channelId={channelId}", channelId)
                )
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$.size()").value(3),
                    jsonPath("$[0].channel.id").value(channelId)
                );
        }
    }
}