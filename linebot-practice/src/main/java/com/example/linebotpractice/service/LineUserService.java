package com.example.linebotpractice.service;

import com.example.linebotpractice.model.LineUser;

public interface LineUserService {
    

    LineUser findLineUserByUserId(String userId);

    void saveLineUser(LineUser lineUser);
}
