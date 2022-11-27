package com.example.linebotpractice.Respository;

import com.example.linebotpractice.model.LineUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineUserRepository  extends MongoRepository<LineUser, String> {
    LineUser findLineUserByUserId(String userId);
}
