package com.example.mytv.adapter.out.jpa.video;

import com.example.mytv.domain.Video;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "video")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VideoJpaEntity {
    @Id
    private String id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String channelId;
    private LocalDateTime publishedAt;

    public Video toDomain() {
        return Video.builder()
            .id(this.getId())
            .title(this.getTitle())
            .description(this.getDescription())
            .thumbnailUrl(this.getThumbnailUrl())
            .channelId(this.channelId)
            .publishedAt(this.getPublishedAt())
            .build();
    }

    public static VideoJpaEntity from(Video video) {
        return new VideoJpaEntity(
            video.getId(),
            video.getTitle(),
            video.getDescription(),
            video.getThumbnailUrl(),
            video.getChannelId(),
            video.getPublishedAt()
        );
    }
}
