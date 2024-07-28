package com.example.mytv.util;

public class CacheNames {
    public static final String SEPARATOR = ":";

    public static final String USER_SESSION = "user-session";

    public static final String CHANNEL = "channel";

    public static final String VIDEO = "video";
    public static final String VIDEO_LIST = VIDEO + SEPARATOR + "list";
    public static final String VIDEO_VIEW_COUNT = VIDEO + SEPARATOR + "view-count";
    public static final String VIDEO_LIKE = VIDEO + SEPARATOR + "like";

    public static final String SUBSCRIBE = "subscribe";
    public static final String SUBSCRIBE_CHANNEL_BY_USER = SUBSCRIBE + SEPARATOR + "channel-by-user";
    public static final String SUBSCRIBE_USER = SUBSCRIBE + SEPARATOR + "user";

    public static final String COMMENT = "comment";
    public static final String COMMENT_LIKE = COMMENT + SEPARATOR + "like";
}
