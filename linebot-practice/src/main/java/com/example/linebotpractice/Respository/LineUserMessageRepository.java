package com.example.linebotpractice.Respository;

import com.example.linebotpractice.model.LineUserMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LineUserMessageRepository extends MongoRepository<LineUserMessage, String> {

    ArrayList<LineUserMessage> findAllByUserId(String userId);
}
