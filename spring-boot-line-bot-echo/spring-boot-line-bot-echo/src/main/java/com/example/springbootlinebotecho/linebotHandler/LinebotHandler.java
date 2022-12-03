package com.example.springbootlinebotecho.linebotHandler;


import com.example.springbootlinebotecho.service.LinebotService;

import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.*;

import com.linecorp.bot.model.event.source.Source;

import com.linecorp.bot.model.message.*;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@LineMessageHandler
@Slf4j
public class LinebotHandler {

    private LinebotService linebotService;

    private LinebotHandler(LinebotService linebotService) {
        this.linebotService = linebotService;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        TextMessageContent message = event.getMessage();
        linebotService.handleTextContent(event.getReplyToken(), event, message);
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        linebotService.handleSticker(event.getReplyToken(), event.getMessage());
    }

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent locationMessage = event.getMessage();
        linebotService.reply(event.getReplyToken(), new LocationMessage(
                locationMessage.getTitle(),
                locationMessage.getAddress(),
                locationMessage.getLatitude(),
                locationMessage.getLongitude()
        ));
    }



    @EventMapping
    public void handleMemberLeft(MemberLeftEvent event) {
        log.info("Got memberLeft message: {}", event.getLeft().getMembers()
                .stream().map(Source::getUserId)
                .collect(Collectors.joining(",")));
    }

    @EventMapping
    public void handleMemberLeft(UnsendEvent event) {
        log.info("Got unsend event: {}", event);
    }

    @EventMapping
    public void handleOtherEvent(Event event) {
        log.info("Received message(Ignored): {}", event);
    }


}
