package com.example.linebotpractice.util;

import com.example.linebotpractice.model.FlexMessageDemo;
import com.example.linebotpractice.model.LineUserMessage;
import com.example.linebotpractice.model.Weather;
import com.example.linebotpractice.model.reply.ReplyBody;
import com.example.linebotpractice.model.reply.ReplyBodyMessage;
import com.example.linebotpractice.service.LineUserMessageService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
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

    public void replyMessage(String replyToken, String text, String userId) throws IOException {
        String respMessage = "";
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
            case "info":
                respMessage = "小噗你好";
                sendResponseInfoMessages(replyToken, respMessage);
                break;
            case "weather":
                respMessage = "";
                sendResponseWeatherMessages(replyToken, respMessage);
                break;
            case "help":
                respMessage = "Currently support: \n" +
                        "1. 訊息紀錄 \n" +
                        "2. 我是噗 \n" +
                        "3. info \n" +
                        "4. weather \n" +
                        "5. help";
                sendResponseTextMessages(replyToken, respMessage);
                break;
            default:
                respMessage = "echo: " + text;
                sendResponseTextMessages(replyToken, respMessage);
                break;
        }
    }

    public void sendResponseTextMessages(String replyToken, String message){
        String modifiedMessage = message + "\n----------\nTesting";
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

    public void sendResponseWeatherMessages(String replyToken, String message) throws IOException {
        HttpUtil httpUtil = new HttpUtil();
        String weatherResponse = httpUtil.get("https://opendata.cwb.gov.tw/api/v1/rest/datastore/O-A0003-001?Authorization=CWB-BEFBC2DC-A35D-45D0-88E1-BD1CCC49891F&locationName=臺北");
        // I get a string which is a json format, transform it to jsonNode
        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = mapper.readTree(weatherResponse);

        String locationName = rootNode.path("records").path("location").get(0).path("locationName").asText();
        System.out.println("locationName: "+locationName);
        String weatherTime = rootNode.path("records").path("location").get(0).path("time").path("obsTime").asText();
        System.out.println("weatherTime: "+weatherTime);
        String TEMP = rootNode.path("records").path("location").get(0).path("weatherElement").get(3).path("elementValue").asText();
        System.out.println("TEMP: "+TEMP);
        String HUMD = rootNode.path("records").path("location").get(0).path("weatherElement").get(4).path("elementValue").asText();
        String HUMDModify = Double.parseDouble(HUMD) * 100 + "%";
        System.out.println("HUMD: "+HUMDModify);
        String weather = rootNode.path("records").path("location").get(0).path("weatherElement").get(20).path("elementValue").asText();
        System.out.println("weather: "+weather);
        String imageUrl = "https://scdn.line-apps.com/n/channel_devcenter/img/fx/01_1_cafe.png";
        switch (weather.substring(0, 1)) {
            case "晴":
                imageUrl = "https://www.cwb.gov.tw/V8/assets/img/weather_icons/weathers/svg_icon/day/01.svg";
                break;
            case "多雲":
                imageUrl = "https://www.cwb.gov.tw/V8/assets/img/weather_icons/weathers/svg_icon/day/04.svg";
                break;
            case "陰":
                imageUrl = "https://www.cwb.gov.tw/V8/assets/img/weather_icons/weathers/svg_icon/day/07.svg";
                break;
            default:

                break;
        }
        try {
            message = "{\"replyToken\":\"" + replyToken + "\",\"messages\":["+ FlexMessageDemo.getWeatherFlexMessageDemo(FlexMessageDemo.DEMO2, locationName, weatherTime, TEMP, HUMDModify, weather, imageUrl ) +"]}";
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

    public void sendResponseInfoMessages(String replyToken, String message) throws IOException {
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
