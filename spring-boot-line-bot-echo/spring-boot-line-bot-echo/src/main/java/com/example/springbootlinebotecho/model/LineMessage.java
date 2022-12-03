package com.example.springbootlinebotecho.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineMessage {

    private String userId;
    private String message;

}
