package com.example.linebotpractice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventWrapper {

    private List<Event> events;

    @Override
    public String toString() {
        return "EventWrapper{" +
                "events=" + events +
                '}';
    }



}
