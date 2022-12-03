package com.example.springbootlinebotadvanced.repository;

import com.example.springbootlinebotadvanced.model.LineMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LineMessageRepository extends MongoRepository<LineMessage, String> {

    ArrayList<LineMessage> findByUserId(String userId);
}

