package com.example.linebotpractice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private String replyToken;
    private String type;
    private Source source;
    private String timestamp;
    private Message message;

    @Override
    public String toString() {
        return "Event{" +
                "replyToken='" + replyToken + '\'' +
                ", type='" + type + '\'' +
                ", source=" + source +
                ", timestamp='" + timestamp + '\'' +
                ", message=" + message +
                '}';
    }

}
