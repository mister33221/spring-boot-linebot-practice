package com.example.linebotpractice.service.serviceImpl;

import com.example.linebotpractice.Respository.LineUserMessageRepository;
import com.example.linebotpractice.model.LineUserMessage;
import com.example.linebotpractice.service.LineUserMessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LineUserMessageServiceImpl implements LineUserMessageService {

    private LineUserMessageRepository lineUserMessageRepository;

    public LineUserMessageServiceImpl(LineUserMessageRepository lineUserMessageRepository) {
        this.lineUserMessageRepository = lineUserMessageRepository;
    }

    @Override
    public void saveLineUserMessage(LineUserMessage lineUserMessage) {
        lineUserMessageRepository.save(lineUserMessage);
    }

    @Override
    public ArrayList<LineUserMessage> getLineUserMessagesById(String userId) {
        return lineUserMessageRepository.findAllByUserId(userId);
    }

}
