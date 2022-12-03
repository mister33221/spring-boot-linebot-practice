package com.example.springbootlinebotadvanced.service;

import com.example.springbootlinebotadvanced.SpringBootLineBotAdvancedApplication;
import com.example.springbootlinebotadvanced.Supplier.ExampleFlexMessageSupplier;
import com.example.springbootlinebotadvanced.Supplier.MessageWithQuickReplySupplier;
import com.example.springbootlinebotadvanced.model.LineMessage;
import com.example.springbootlinebotadvanced.model.LineUser;
import com.example.springbootlinebotadvanced.model.flexMessageModel.GithubPage;
import com.example.springbootlinebotadvanced.repository.LineMessageRepository;
import com.example.springbootlinebotadvanced.repository.LineUserRepository;
import com.google.common.io.ByteStreams;
import com.linecorp.bot.client.LineBlobClient;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.group.GroupMemberCountResponse;
import com.linecorp.bot.model.group.GroupSummaryResponse;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.message.imagemap.*;
import com.linecorp.bot.model.message.sender.Sender;
import com.linecorp.bot.model.message.template.*;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.room.RoomMemberCountResponse;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

@Service
@Slf4j
public class LinebotService {


    private LineMessagingClient lineMessagingClient;


    private LineBlobClient lineBlobClient;


    private LineUserRepository userRepository;

    private LineMessageRepository lineMessageRepository;

    @Autowired
    public LinebotService(LineMessagingClient lineMessagingClient, LineBlobClient lineBlobClient, LineUserRepository userRepository, LineMessageRepository lineMessageRepository) {
        this.lineMessagingClient = lineMessagingClient;
        this.lineBlobClient = lineBlobClient;
        this.userRepository = userRepository;
        this.lineMessageRepository = lineMessageRepository;
    }

    public void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        reply(replyToken, messages, false);
    }

    private void reply(@NonNull String replyToken,
                       @NonNull List<Message> messages,
                       boolean notificationDisabled) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages, notificationDisabled))
                    .get();
            log.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    public void handleHeavyContent(String replyToken, String messageId,
                                   Consumer<MessageContentResponse> messageConsumer) {
        final MessageContentResponse response;
        try {
            response = lineBlobClient.getMessageContent(messageId)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
            throw new RuntimeException(e);
        }
        messageConsumer.accept(response);
    }

    public void handleSticker(String replyToken, StickerMessageContent content) {
        log.info("Got sticker message: packageId={}, stickerId={}", content.getPackageId(),
                content.getStickerId());
        replyText(replyToken, "Got sticker message: packageId=" + content.getPackageId() +
                ", stickerId=" + content.getStickerId());
        // 只能使用免費的貼圖序號
//        reply(replyToken, new StickerMessage(
//                content.getPackageId(), content.getStickerId()));
//        reply(replyToken, new StickerMessage(
//                content.getPackageId(), "51626532"));
    }

    public void handleTextContent(String replyToken, Event event, TextMessageContent content)
            throws Exception {
        final String text = content.getText().trim();

        // log
        log.info("Got text message from replyToken:{}: text:{} emojis:{}", replyToken, text,
                content.getEmojis());
        log.info("Got text message from event:{}", event);
        // save user id
        LineUser lineUser = new LineUser(event.getSource().getUserId(), lineMessagingClient.getProfile(event.getSource().getUserId()).get().getDisplayName());
        if (userRepository.findByUserId(lineUser.getUserId()) == null) {
            userRepository.save(lineUser);
        }
        // save message
        LineMessage lineMessage = new LineMessage(lineUser.getUserId(), text);
        lineMessageRepository.save(lineMessage);
        switch (text) {
            case "help": {
                replyText(replyToken, "請輸入以下指令:\n" +
                        "1. help\n" +
                        "2. profile\n" +
                        "3. bye(group only)\n" +
                        "4. group_summary(group only)\n" +
                        "5. group_member_count(group only)\n" +
                        "6. room_summary(room only)\n" +
                        "7. room_member_count(room only)\n" +
                        "8. confirm\n" +
                        "9. buttons\n" +
                        "10. carousel\n" +
                        "11. image_carousel\n" +
                        "12. imagemap\n" +
                        "13. imagemap_video\n" +
                        "14. flex\n" +
                        "15. quickreply\n" +
                        "16. no_notify\n" +
                        "17. redelivery\n" +
                        "18. icon\n" +
                        "19. github_page\n" +
                        "20. history_message\n"
                );
            }
            case "profile": {
                log.info("Invoking 'profile' command: source:{}",
                        event.getSource());
                final String userId = event.getSource().getUserId();
                if (userId != null) {
                    if (event.getSource() instanceof GroupSource) {
                        lineMessagingClient
                                .getGroupMemberProfile(((GroupSource) event.getSource()).getGroupId(), userId)
                                .whenComplete((profile, throwable) -> {
                                    if (throwable != null) {
                                        this.replyText(replyToken, throwable.getMessage());
                                        return;
                                    }

                                    this.reply(
                                            replyToken,
                                            Arrays.asList(new TextMessage("(from group)"),
                                                    new TextMessage(
                                                            "Display name: " + profile.getDisplayName()),
                                                    new ImageMessage(profile.getPictureUrl(),
                                                            profile.getPictureUrl()))
                                    );
                                });
                    } else {
                        lineMessagingClient
                                .getProfile(userId)
                                .whenComplete((profile, throwable) -> {
                                    if (throwable != null) {
                                        this.replyText(replyToken, throwable.getMessage());
                                        return;
                                    }

                                    this.reply(
                                            replyToken,
                                            Arrays.asList(new TextMessage(
                                                            "Display name: " + profile.getDisplayName()),
                                                    new TextMessage("Status message: "
                                                            + profile.getStatusMessage()))
                                    );

                                });
                    }
                } else {
                    this.replyText(replyToken, "Bot can't use profile API without user ID");
                }
                break;
            }
            case "bye": {
                Source source = event.getSource();
                if (source instanceof GroupSource) {
                    this.replyText(replyToken, "Leaving group");
                    lineMessagingClient.leaveGroup(((GroupSource) source).getGroupId()).get();
                } else if (source instanceof RoomSource) {
                    this.replyText(replyToken, "Leaving room");
                    lineMessagingClient.leaveRoom(((RoomSource) source).getRoomId()).get();
                } else {
                    this.replyText(replyToken, "Bot can't leave from 1:1 chat");
                }
                break;
            }
            case "group_summary": {
                Source source = event.getSource();
                if (source instanceof GroupSource) {
                    GroupSummaryResponse groupSummary = lineMessagingClient.getGroupSummary(
                            ((GroupSource) source).getGroupId()).get();
                    this.replyText(replyToken, "Group summary: " + groupSummary);
                } else {
                    this.replyText(replyToken, "You can't use 'group_summary' command for "
                            + source);
                }
                break;
            }
            case "group_member_count": {
                Source source = event.getSource();
                if (source instanceof GroupSource) {
                    GroupMemberCountResponse groupMemberCountResponse = lineMessagingClient.getGroupMemberCount(
                            ((GroupSource) source).getGroupId()).get();
                    this.replyText(replyToken, "Group member count: "
                            + groupMemberCountResponse.getCount());
                } else {
                    this.replyText(replyToken, "You can't use 'group_member_count' command  for "
                            + source);
                }
                break;
            }
            case "room_member_count": {
                Source source = event.getSource();
                if (source instanceof RoomSource) {
                    RoomMemberCountResponse roomMemberCountResponse = lineMessagingClient.getRoomMemberCount(
                            ((RoomSource) source).getRoomId()).get();
                    this.replyText(replyToken, "Room member count: "
                            + roomMemberCountResponse.getCount());
                } else {
                    this.replyText(replyToken, "You can't use 'room_member_count' command  for "
                            + source);
                }
                break;
            }
            case "confirm": {
                ConfirmTemplate confirmTemplate = new ConfirmTemplate(
                        "Do it?",
                        new MessageAction("Yes", "Yes!"),
                        new MessageAction("No", "No!")
                );
                TemplateMessage templateMessage = new TemplateMessage("Confirm alt text", confirmTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "buttons": {
                URI imageUrl = createUri("/static/buttons/1040.jpg");
                ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
                        imageUrl,
                        "My button sample",
                        "Hello, my button",
                        Arrays.asList(
                                new URIAction("Go to line.me",
                                        URI.create("https://line.me"), null),
                                new PostbackAction("Say hello1",
                                        "hello こんにちは"),
                                new PostbackAction("言 hello2",
                                        "hello こんにちは",
                                        "hello こんにちは"),
                                new MessageAction("Say message",
                                        "Rice=米")
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "carousel": {
                URI imageUrl = createUri("/static/buttons/1040.jpg");
                CarouselTemplate carouselTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                        new URIAction("Go to line.me",
                                                URI.create("https://line.me"), null),
                                        new URIAction("Go to line.me",
                                                URI.create("https://line.me"), null),
                                        new PostbackAction("Say hello1",
                                                "hello こんにちは")
                                )),
                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                        new PostbackAction("言 hello2",
                                                "hello こんにちは",
                                                "hello こんにちは"),
                                        new PostbackAction("言 hello2",
                                                "hello こんにちは",
                                                "hello こんにちは"),
                                        new MessageAction("Say message",
                                                "Rice=米")
                                )),
                                new CarouselColumn(imageUrl, "Datetime Picker",
                                        "Please select a date, time or datetime", Arrays.asList(
                                        DatetimePickerAction.OfLocalDatetime
                                                .builder()
                                                .label("Datetime")
                                                .data("action=sel")
                                                .initial(LocalDateTime.parse("2017-06-18T06:15"))
                                                .min(LocalDateTime.parse("1900-01-01T00:00"))
                                                .max(LocalDateTime.parse("2100-12-31T23:59"))
                                                .build(),
                                        DatetimePickerAction.OfLocalDate
                                                .builder()
                                                .label("Date")
                                                .data("action=sel&only=date")
                                                .initial(LocalDate.parse("2017-06-18"))
                                                .min(LocalDate.parse("1900-01-01"))
                                                .max(LocalDate.parse("2100-12-31"))
                                                .build(),
                                        DatetimePickerAction.OfLocalTime
                                                .builder()
                                                .label("Time")
                                                .data("action=sel&only=time")
                                                .initial(LocalTime.parse("06:15"))
                                                .min(LocalTime.parse("00:00"))
                                                .max(LocalTime.parse("23:59"))
                                                .build()
                                ))
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "image_carousel": {
                URI imageUrl = createUri("/static/buttons/1040.jpg");
                ImageCarouselTemplate imageCarouselTemplate = new ImageCarouselTemplate(
                        Arrays.asList(
                                new ImageCarouselColumn(imageUrl,
                                        new URIAction("Goto line.me",
                                                URI.create("https://line.me"), null)
                                ),
                                new ImageCarouselColumn(imageUrl,
                                        new MessageAction("Say message",
                                                "Rice=米")
                                ),
                                new ImageCarouselColumn(imageUrl,
                                        new PostbackAction("言 hello2",
                                                "hello こんにちは",
                                                "hello こんにちは")
                                )
                        ));
                TemplateMessage templateMessage = new TemplateMessage("ImageCarousel alt text",
                        imageCarouselTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "imagemap":
                //            final String baseUrl,
                //            final String altText,
                //            final ImagemapBaseSize imagemapBaseSize,
                //            final List<ImagemapAction> actions) {
                this.reply(replyToken, ImagemapMessage
                        .builder()
                        .baseUrl(createUri("/static/rich"))
                        .altText("This is alt text")
                        .baseSize(new ImagemapBaseSize(1040, 1040))
                        .actions(Arrays.asList(
                                URIImagemapAction.builder()
                                        .linkUri("https://store.line.me/family/manga/en")
                                        .area(new ImagemapArea(0, 0, 520, 520))
                                        .build(),
                                URIImagemapAction.builder()
                                        .linkUri("https://store.line.me/family/music/en")
                                        .area(new ImagemapArea(520, 0, 520, 520))
                                        .build(),
                                URIImagemapAction.builder()
                                        .linkUri("https://store.line.me/family/play/en")
                                        .area(new ImagemapArea(0, 520, 520, 520))
                                        .build(),
                                MessageImagemapAction.builder()
                                        .text("URANAI!")
                                        .area(new ImagemapArea(520, 520, 520, 520))
                                        .build()
                        ))
                        .build());
                break;
            case "imagemap_video":
                this.reply(replyToken, ImagemapMessage
                        .builder()
                        .baseUrl(createUri("/static/imagemap_video"))
                        .altText("This is an imagemap with video")
                        .baseSize(new ImagemapBaseSize(722, 1040))
                        .video(
                                ImagemapVideo.builder()
                                        .originalContentUrl(
                                                createUri("/static/imagemap_video/originalContent.mp4"))
                                        .previewImageUrl(
                                                createUri("/static/imagemap_video/previewImage.jpg"))
                                        .area(new ImagemapArea(40, 46, 952, 536))
                                        .externalLink(
                                                new ImagemapExternalLink(
                                                        URI.create("https://example.com/see_more.html"),
                                                        "See More")
                                        )
                                        .build()
                        )
                        .actions(singletonList(
                                MessageImagemapAction.builder()
                                        .text("NIXIE CLOCK")
                                        .area(new ImagemapArea(260, 600, 450, 86))
                                        .build()
                        ))
                        .build());
                break;
            case "flex":
                this.reply(replyToken, new ExampleFlexMessageSupplier().get());
                break;
            case "quickreply":
                this.reply(replyToken, new MessageWithQuickReplySupplier().get());
                break;
            case "no_notify":
                this.reply(replyToken,
                        singletonList(new TextMessage("This message is send without a push notification")),
                        true);
                break;
            case "redelivery":
                this.reply(replyToken,
                        singletonList(new TextMessage("webhookEventId=" + event.getWebhookEventId()
                                + " deliveryContext.isRedelivery=" + event.getDeliveryContext().getIsRedelivery())
                        ));
                break;
            case "icon":
                this.reply(replyToken,
                        TextMessage.builder()
                                .text("Hello, I'm cat! Meow~")
                                .sender(Sender.builder()
                                        .name("Cat")
                                        .iconUrl(createUri("/static/icon/cat.png"))
                                        .build())
                                .build());
                break;
            case "github_page":
                this.reply(replyToken, new GithubPage().get());
                break;
            case "我是噗":
                this.reply(replyToken, new StickerMessage("1", "409"));
                break;
            case "history_message":
                ArrayList<LineMessage> lineMessages = lineMessageRepository.findByUserId(lineUser.getUserId());
                String modifyMessage = "";
                for (LineMessage lineMessageElement : lineMessages) {
                    modifyMessage = modifyMessage + lineMessageElement.getMessage() + "\n";
                }
                this.replyText(replyToken, modifyMessage);
                break;
            default:
                log.info("Returns echo message {}: {}", replyToken, text);
                this.replyText(
                        replyToken,
                        text
                );
                break;
        }
    }

    public static URI createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .scheme("https")
                .path(path).build()
                .toUri();
    }

    public void system(String... args) {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        try {
            Process start = processBuilder.start();
            int i = start.waitFor();
            log.info("result: {} =>  {}", Arrays.toString(args), i);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            log.info("Interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    public static DownloadedContent saveContent(String ext, MessageContentResponse responseBody) {
        log.info("Got content-type: {}", responseBody);

        DownloadedContent tempFile = createTempFile(ext);
        try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
            ByteStreams.copy(responseBody.getStream(), outputStream);
            log.info("Saved {}: {}", ext, tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static DownloadedContent createTempFile(String ext) {
        String fileName = LocalDateTime.now().toString() + '-' + UUID.randomUUID() + '.' + ext;
        Path tempFile = SpringBootLineBotAdvancedApplication.downloadedContentDir.resolve(fileName);
        tempFile.toFile().deleteOnExit();
        return new DownloadedContent(
                tempFile,
                createUri("/downloaded/" + tempFile.getFileName()));
    }

    @Value
    private static class DownloadedContent {
        Path path;
        URI uri;
    }
}
