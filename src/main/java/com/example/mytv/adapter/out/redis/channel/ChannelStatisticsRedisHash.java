package com.example.mytv.adapter.out.redis.channel;

import com.example.mytv.domain.channel.ChannelStatistics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("channelStatistics")
@AllArgsConstructor
@Getter
public class ChannelStatisticsRedisHash {
    private int viewCount;
    private int videoCount;
    private int subscriberCount;
    private int commentCount;

    public static ChannelStatisticsRedisHash from(ChannelStatistics statistics) {
        return new ChannelStatisticsRedisHash(statistics.getViewCount(), statistics.getVideoCount(), statistics.getSubscriberCount(), statistics.getCommentCount());
    }

    public ChannelStatistics toDomain() {
        return ChannelStatistics.builder()
            .viewCount(this.getViewCount())
            .videoCount(this.getVideoCount())
            .subscriberCount(this.getSubscriberCount())
            .commentCount(this.getCommentCount())
            .build();
    }
}