package com.example.springbootlinebotadvanced.model.flexMessageModel;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;

import java.net.URI;
import java.util.function.Supplier;

import static com.example.springbootlinebotadvanced.service.LinebotService.createUri;
import static java.util.Arrays.asList;

public class GithubPage implements Supplier<FlexMessage> {

    @Override
    public FlexMessage get() {
        final Image heroBlock =
                Image.builder()
                        // get the image from resource/static/photo
                        .url(createUri("/static/photo/githubPageBanner.jpg") )
                        .size(Image.ImageSize.FULL_WIDTH)
                        .aspectRatio(Image.ImageAspectRatio.R20TO13)
                        .aspectMode(Image.ImageAspectMode.Cover)
                        .action(new URIAction("label", URI.create("https://mister33221.github.io/index.html"), null))
                        .build();

        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock();
        final Bubble bubble =
                Bubble.builder()
                        .hero(heroBlock)
                        .body(bodyBlock)
                        .footer(footerBlock)
                        .build();

        return new FlexMessage("ALT", bubble);
    }

    private Box createFooterBlock() {
        final Spacer spacer = Spacer.builder().size(FlexMarginSize.SM).build();
//        final Button callAction = Button
//                .builder()
//                .style(Button.ButtonStyle.LINK)
//                .height(Button.ButtonHeight.SMALL)
//                .action(new URIAction("CALL", URI.create("tel:000000"), null))
//                .build();
        final Separator separator = Separator.builder().build();
        final Button websiteAction =
                Button.builder()
                        .style(Button.ButtonStyle.LINK)
                        .height(Button.ButtonHeight.SMALL)
                        .action(new URIAction("WEBSITE", URI.create("https://mister33221.github.io/index.html"), null))
                        .build();

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .spacing(FlexMarginSize.SM)
                .contents(asList(spacer,  separator, websiteAction))
                .build();
    }

    private Box createBodyBlock() {
        final Text title =
                Text.builder()
                        .text("Kai's Github Page")
                        .weight(Text.TextWeight.BOLD)
                        .size(FlexFontSize.XL)
                        .build();

        final Box review = createReviewBox();

        final Box info = createInfoBox();

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(asList(title, review, info))
                .build();
    }

    private Box createInfoBox() {
        final Box place = Box
                .builder()
                .layout(FlexLayout.BASELINE)
                .spacing(FlexMarginSize.SM)
                .contents(asList(
                        Text.builder()
                                .text("Place")
                                .color("#aaaaaa")
                                .size(FlexFontSize.SM)
                                .flex(1)
                                .build(),
                        Text.builder()
                                .text("New Taipei, Taiwan")
                                .wrap(true)
                                .color("#666666")
                                .size(FlexFontSize.SM)
                                .flex(5)
                                .build()
                ))
                .build();
        final Box time =
                Box.builder()
                        .layout(FlexLayout.BASELINE)
                        .spacing(FlexMarginSize.SM)
                        .contents(asList(
                                Text.builder()
                                        .text("Title")
                                        .color("#aaaaaa")
                                        .size(FlexFontSize.SM)
                                        .flex(1)
                                        .build(),
                                Text.builder()
                                        .text("Backend Web Developer")
                                        .wrap(true)
                                        .color("#666666")
                                        .size(FlexFontSize.SM)
                                        .flex(5)
                                        .build()
                        ))
                        .build();

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .margin(FlexMarginSize.LG)
                .spacing(FlexMarginSize.SM)
                .contents(asList(place, time))
                .build();
    }

    private Box createReviewBox() {
        final Icon goldStar =
                Icon.builder().size(FlexFontSize.SM).url(URI.create("https://example.com/gold_star.png")).build();
        final Icon grayStar =
                Icon.builder().size(FlexFontSize.SM).url(URI.create("https://example.com/gray_star.png")).build();
//        final Text point =
//                Text.builder()
//                        .text("4.0")
//                        .size(FlexFontSize.SM)
//                        .color("#999999")
//                        .margin(FlexMarginSize.MD)
//                        .flex(0)
//                        .build();

        return Box.builder()
                .layout(FlexLayout.BASELINE)
                .margin(FlexMarginSize.MD)
                .contents(asList(goldStar, goldStar, goldStar, goldStar, grayStar))
                .build();
    }
}
