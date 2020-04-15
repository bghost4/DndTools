package com.derp.hurr.whiteboard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WhiteboardClient extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        WhiteBoard wb = new WhiteBoard();

        Scene s = new Scene(wb);
        primaryStage.setTitle("Whiteboard");
        primaryStage.setScene(s);

        primaryStage.show();


    }



    public static void main(String[] args) {
        launch(args);
    }
}
