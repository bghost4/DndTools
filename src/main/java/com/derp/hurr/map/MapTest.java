package com.derp.hurr.map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MapTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Mapbuilder mb = new Mapbuilder();

        primaryStage.setTitle("Map Tester");
        Scene s = new Scene(mb);
        primaryStage.setScene(s);

        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }


}
