package com.example.springbootlinebotecho;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
@SpringBootApplication
@LineMessageHandler
public class SpringBootLineBotEchoApplication {

	private final Logger log = LoggerFactory.getLogger(SpringBootLineBotEchoApplication.class);

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SpringBootLineBotEchoApplication.class, args);
	}

	@EventMapping
	public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		log.info("event: " + event);
		final String originalMessageText = event.getMessage().getText();
		return new TextMessage(originalMessageText);
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}

}
