package com.example.springbootlinebotecho.repository;

import com.example.springbootlinebotecho.model.LineUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineUserRepository extends MongoRepository<LineUser, String> {


    LineUser findByUserId(String userId);
}
