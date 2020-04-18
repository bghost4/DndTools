package com.derp.hurr.whiteboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


import java.io.IOException;

public class JacksonPlay {

    public static void main(String[] args) {

        ObjectMapper m = new ObjectMapper();
        m.enableDefaultTyping();

        Paint p = Color.BLUE;

        try {
            String s = m.writerWithDefaultPrettyPrinter().writeValueAsString(p);

            System.out.println("Data: "+s);

            Color color = m.readValue(s,Color.class);
            System.out.println("Color: "+color.toString());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
