package com.example.linebotpractice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Source {

    private String type;
    private String userId;
    private String groupId;
    private String roomId;

}
