package com.example.linebotpractice.model.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyBodyMessage {

    private String type;
    private String altText;
    private String text;
    private String content;

}
