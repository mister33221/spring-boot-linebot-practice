package com.example.linebotpractice.service;

import com.example.linebotpractice.model.LineUserMessage;

import java.util.ArrayList;

public interface LineUserMessageService {
    void saveLineUserMessage(LineUserMessage lineUserMessage);

    ArrayList<LineUserMessage> getLineUserMessagesById(String userId);
}
