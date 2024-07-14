package com.example.mytv.adapter.in.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChannelRequest {
    private ChannelSnippetRequest snippet;
    private String contentOwnerId;
}
