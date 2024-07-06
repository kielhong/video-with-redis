package com.example.video.adapter.out;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.video.adapter.out.jpa.video.VideoJpaEntityFixtures;
import com.example.video.adapter.out.jpa.video.VideoJpaRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VideoPersistenceAdapterTest {
    private VideoPersistenceAdapter sut;

    @Mock
    private VideoJpaRepository videoJpaRepository;

    @BeforeEach
    void setUp() {
        sut = new VideoPersistenceAdapter(videoJpaRepository);
    }

    @Nested
    class LoadVideo {
        @Test
        void existVideo_then_returnVideo() {
            // Given
            var videoJpaEntity = VideoJpaEntityFixtures.stub("video1");
            given(videoJpaRepository.findById(any()))
                .willReturn(Optional.of(videoJpaEntity));

            // When
            var result = sut.loadVideo("video1");

            // Then
            then(result)
                .extracting("id")
                .isEqualTo("video1");
        }

        @Test
        void notExistVideo_then_throwException() {
            // Given
            given(videoJpaRepository.findById(any()))
                .willReturn(Optional.empty());

            // When
            var result = thenThrownBy(() -> sut.loadVideo("video1"));

            // Then
            result.isInstanceOf(NoSuchElementException.class);
        }
    }

    @Test
    void loadVideoByChannel() {
        // Given
        var videoJpaEntity1 = VideoJpaEntityFixtures.stub("video1");
        var videoJpaEntity2 = VideoJpaEntityFixtures.stub("video2");
        given(videoJpaRepository.findByChannelId(any()))
            .willReturn(List.of(videoJpaEntity1, videoJpaEntity2));

        // When
        var result = sut.loadVideoByChannel("channelId");

        // Then
        then(result)
            .hasSize(2)
            .extracting("id")
            .containsExactly("video1", "video2");
    }
}