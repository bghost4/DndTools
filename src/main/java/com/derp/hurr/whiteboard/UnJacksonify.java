package com.derp.hurr.whiteboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import java.io.File;

public class UnJacksonify extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        ObjectMapper om = new ObjectMapper();
        om.enableDefaultTyping();

        Figure f = om.readValue(new File("JacksonPath.json"),Figure.class);

        Group g = new Group();
        Path p = new Path();
        p.getElements().addAll(f.getPathElements());
        p.setStroke(Color.BLACK);
        p.setStrokeWidth(5);

        g.getChildren().add(p);

        Scene s = new Scene(g);

        primaryStage.setScene(s);
        primaryStage.setTitle("Jacksonify Path Test");
        primaryStage.show();;





    }

    public static void main(String[] args) {
        launch(args);
    }



}
