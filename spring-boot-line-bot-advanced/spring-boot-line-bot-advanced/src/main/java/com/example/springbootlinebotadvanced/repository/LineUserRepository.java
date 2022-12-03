package com.example.springbootlinebotadvanced.repository;


import com.example.springbootlinebotadvanced.model.LineUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineUserRepository extends MongoRepository<LineUser, String> {


    LineUser findByUserId(String userId);
}
