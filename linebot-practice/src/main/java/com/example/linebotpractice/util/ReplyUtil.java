package com.example.linebotpractice.util;

import com.example.linebotpractice.model.FlexMessageDemo;
import com.example.linebotpractice.model.LineUserMessage;
import com.example.linebotpractice.model.reply.ReplyBody;
import com.example.linebotpractice.model.reply.ReplyBodyMessage;
import com.example.linebotpractice.service.LineUserMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

@Component
public class ReplyUtil {

    private final String accessToken = "72MqqqLaIhqcgd+oXb6vs0CfNvciWhCanRBt1Ll7vSWe0IN/InrZ4eW++jPBtohpANWpsALyhZhPdoC6fuYXJNwHv/GK/k9PTfT16eG0g/7zlQu3ECZwnkWKW8uyMCVb8NVarfGKSbFEk5qB6OZnHgdB04t89/1O/w1cDnyilFU=";

    private LineUserMessageService lineUserMessageService;

    public ReplyUtil(LineUserMessageService lineUserMessageService) {
        this.lineUserMessageService = lineUserMessageService;
    }

    public HttpsURLConnection setConnection(URL url, String accessToken) throws IOException {
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + accessToken);
        con.setDoOutput(true);
        return con;
    }

    public void replyMessage(String replyToken, String text, String userId) {
        String respMessage = null;
        switch (text) {
            case "訊息紀錄":
                ArrayList<LineUserMessage> lineUserMessages = lineUserMessageService.getLineUserMessagesById(userId);
                StringBuilder sb = new StringBuilder();
                Integer index = 1;
                for (LineUserMessage lineUserMessage : lineUserMessages) {
                    sb.append(index +". " +lineUserMessage.getText()).append(", \n");
                    index++;
                }
                respMessage = sb.toString();
                sendResponseTextMessages(replyToken, respMessage);
                break;
            case "我是噗":
                respMessage = "小噗你好";
                sendResponseTextMessages(replyToken, respMessage);
                break;
            case "flex":
                respMessage = "小噗你好";
                sendResponseFlexMessages(replyToken, respMessage);
                break;
            default:
                respMessage = "echo: " + text;
                sendResponseTextMessages(replyToken, respMessage);
                break;
        }
    }

    public void sendResponseTextMessages(String replyToken, String message){
        String modifiedMessage = message + "----Testing";
        ObjectMapper mapper = new ObjectMapper();
        ReplyBody replyBody = new ReplyBody();
        ReplyBodyMessage replyBodyMessage = new ReplyBodyMessage();
        replyBody.setReplyToken(replyToken);
        replyBodyMessage.setType("text");
        replyBodyMessage.setAltText("This is a text message");
        replyBodyMessage.setText(modifiedMessage);
        replyBody.getMessages().add(replyBodyMessage);

        try {
            URL myurl = new URL("https://api.line.me/v2/bot/message/reply"); //回傳的網址
            HttpsURLConnection con = setConnection(myurl, this.accessToken);//使用HttpsURLConnection建立https連線
            DataOutputStream output = new DataOutputStream(con.getOutputStream()); //開啟HttpsURLConnection的連線
            output.write(mapper.writeValueAsString(replyBody).getBytes(Charset.forName("utf-8")));  //回傳訊息編碼為utf-8
            output.close();
            System.out.println("Resp Code:" + con.getResponseCode() + "; Resp Message:" + con.getResponseMessage()); //顯示回傳的結果，若code為200代表回傳成功
        } catch (MalformedURLException e) {
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }


    }

    public void sendResponseFlexMessages(String replyToken, String message){
//        ObjectMapper mapper = new ObjectMapper();
//        ReplyBody replyBody = new ReplyBody();
//        ReplyBodyMessage replyBodyMessage = new ReplyBodyMessage();
//        replyBody.setReplyToken(replyToken);
//        replyBodyMessage.setType("flex");
//        replyBodyMessage.setAltText("This is a flex message");
//        replyBodyMessage.setContent(FlexMessageDemo.getFlexMessageDemo(FlexMessageDemo.DEMO2));

        try {
            message = "{\"replyToken\":\"" + replyToken + "\",\"messages\":["+ FlexMessageDemo.getFlexMessageDemo(FlexMessageDemo.DEMO2 ) +"]}";
            URL myurl = new URL("https://api.line.me/v2/bot/message/reply"); //回傳的網址
            HttpsURLConnection con = setConnection(myurl, this.accessToken);//使用HttpsURLConnection建立https連線
            DataOutputStream output = new DataOutputStream(con.getOutputStream()); //開啟HttpsURLConnection的連線
            output.write(message.getBytes(Charset.forName("utf-8")));  //回傳訊息編碼為utf-8
            output.close();
            System.out.println("Resp Code:" + con.getResponseCode() + "; Resp Message:" + con.getResponseMessage()); //顯示回傳的結果，若code為200代表回傳成功
            System.out.println("Resp Code:" + con.getContent().toString());
        } catch (MalformedURLException e) {
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
