package com.derp.hurr.whiteboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class JacksonifyPath {

    public static void main(String[] args) {

        Figure f = createFigure();

        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();

        try {

            mapper.writeValue(new File("JacksonPath.json"),f);
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(f));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static Figure createFigure() {

        Path p = new Path();
        p.getElements().addAll(
            new MoveTo(0.0,0.0),
            new LineTo(100.0,0.0),
            new LineTo(0.0,100.0),
            new LineTo(100.0,100.0),
            new LineTo(0.0,0.0)
        );

        Figure f = new Figure(p, UUID.randomUUID());

        return f;

    }

}
