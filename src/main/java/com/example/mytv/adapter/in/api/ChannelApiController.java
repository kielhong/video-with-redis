package com.example.mytv.adapter.in.api;

import com.example.mytv.adapter.in.api.dto.ChannelRequest;
import com.example.mytv.application.port.in.ChannelUseCase;
import com.example.mytv.domain.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channels")
@RequiredArgsConstructor
public class ChannelApiController {
    private final ChannelUseCase channelUseCase;

    @PostMapping
    public void createChannel(@RequestBody ChannelRequest channelRequest) {
        channelUseCase.createChannel(channelRequest);
    }

    @PutMapping("{channelId}")
    public void updateChannel(
        @PathVariable String channelId,
        @RequestBody ChannelRequest channelRequest
    ) {
        channelUseCase.updateChannel(channelId, channelRequest);
    }

    @GetMapping("{channelId}")
    public Channel getChannel(@PathVariable String channelId) {
        return channelUseCase.getChannel(channelId);
    }
}
