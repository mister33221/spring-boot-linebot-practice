package com.example.linebotpractice.model.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyBody {

    private String replyToken;
    private List<ReplyBodyMessage> messages = new ArrayList<>();

}

