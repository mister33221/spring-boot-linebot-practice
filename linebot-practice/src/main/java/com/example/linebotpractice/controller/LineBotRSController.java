package com.example.linebotpractice.controller;

import com.example.linebotpractice.model.Event;
import com.example.linebotpractice.model.EventWrapper;
import com.example.linebotpractice.model.LineUser;
import com.example.linebotpractice.model.LineUserMessage;
import com.example.linebotpractice.service.LineUserMessageService;
import com.example.linebotpractice.service.LineUserService;
import com.example.linebotpractice.util.ReplyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

@RestController
public class LineBotRSController {

    private ReplyUtil replyUtil;

    private LineUserService lineUserService;

    private LineUserMessageService lineUserMessageService;


    public LineBotRSController(ReplyUtil replyUtil, LineUserService lineUserService, LineUserMessageService lineUserMessageService) {
        this.replyUtil = replyUtil;
        this.lineUserService = lineUserService;
        this.lineUserMessageService = lineUserMessageService;
    }

//    v1
//    @RequestMapping("/callback")
//    public void callback(@RequestBody String message) {
//        System.out.println("message: " + message);
//    }

//    v2
//    @RequestMapping("/callback")
//    public void callback(@RequestBody EventWrapper events) {
//        for (Event event : events.getEvents()) {
//            System.out.println("event: " + event.getType());
//            System.out.println("event: " + event);
//            System.out.println("getReplyToken: " + event.getReplyToken());
//        }
//
//    }

    //    v3
    @RequestMapping("/callback")
    public void callback(@RequestBody EventWrapper events) {
        System.out.println("This is messages event");
        for (Event event : events.getEvents()) {
            if ("message".equals(event.getType()) && "user".equals(event.getSource().getType())) {
                LineUser lineUser = lineUserService.findLineUserByUserId(event.getSource().getUserId());
                if (lineUser == null) {
                    lineUser = new LineUser();
                    lineUser.setUserId(event.getSource().getUserId());
                    lineUserService.saveLineUser(lineUser);
                    LineUserMessage lineUserMessage = new LineUserMessage(event.getMessage().getId(), event.getSource().getUserId(), event.getMessage().getType(), event.getMessage().getText());
                    lineUserMessageService.saveLineUserMessage(lineUserMessage);
                } else {
                    LineUserMessage lineUserMessage = new LineUserMessage(event.getMessage().getId(), lineUser.getUserId(), event.getMessage().getType(), event.getMessage().getText());
                    lineUserMessageService.saveLineUserMessage(lineUserMessage);
                }
            }
            switch (event.getMessage().getType()) {
                case "text":
                    System.out.println("event: " + event);
                    replyUtil.replyMessage(event.getReplyToken(), event.getMessage().getText(), event.getSource().getUserId());
//                    replyUtil.sendResponseTextMessages(event.getReplyToken(), respMessage);
                    break;
                case "image":
                    System.out.println("This is image event, It's replyToken is " + event.getReplyToken());
                    break;
                case "audio":
                    System.out.println("This is audio event, It's replyToken is " + event.getReplyToken());
                    break;
                case "file":
                    System.out.println("This is file event, It's replyToken is " + event.getReplyToken());
                    break;
                case "sticker":
                    System.out.println("This is sticker event, It's replyToken is " + event.getReplyToken());
                    break;
                case "location":
                    System.out.println("This is location event, It's replyToken is " + event.getReplyToken());
                    break;
                default:
                    System.out.println("This is other event, It's replyToken is " + event.getReplyToken());
                    break;
            }
        }
    }



}
