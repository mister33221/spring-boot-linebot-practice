package com.example.linebotpractice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String id;
    private String type;
    private String text;
    private String filename;
    private String filesize;
    private String title;
    private String address;
    private String latitude;
    private String longitude;
    private String packageId;
    private String stickerId;

}
