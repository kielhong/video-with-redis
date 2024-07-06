package com.example.mytv.adapter.in.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChannelSnippetRequest {
    private String title;
    private String description;
    private String thumbnailUrl;
}
