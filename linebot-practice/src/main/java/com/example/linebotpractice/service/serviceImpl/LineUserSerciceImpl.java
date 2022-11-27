package com.example.linebotpractice.service.serviceImpl;

import com.example.linebotpractice.Respository.LineUserRepository;
import com.example.linebotpractice.model.LineUser;
import com.example.linebotpractice.service.LineUserService;
import org.springframework.stereotype.Service;

@Service
public class LineUserSerciceImpl implements LineUserService {

    private LineUserRepository lineUserRepository;

    public LineUserSerciceImpl(LineUserRepository lineUserRepository) {
        this.lineUserRepository = lineUserRepository;
    }


    @Override
    public LineUser findLineUserByUserId(String userId) {
        return lineUserRepository.findLineUserByUserId(userId);
    }

    @Override
    public void saveLineUser(LineUser lineUser) {
        lineUserRepository.save(lineUser);
    }
}
