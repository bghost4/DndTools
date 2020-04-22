package com.derp.hurr;

import com.derp.hurr.whiteboard.WhiteBoardInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OSDungeonLauncher extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox vb = new VBox();

        Button btnClient = new Button("Launch as Player");
        Button btnDM = new Button("Launch as DM Screen");
        Button btnServer = new Button("Launch Server Only");
        Button btnQuit = new Button("Quit");

        btnQuit.setOnAction(eh -> {
            Platform.exit();
            System.exit(0);
        });

        btnClient.setOnAction(eh -> {
            try {
                Stage window = new Stage();
                WhiteBoardInterface wb = new WhiteBoardInterface();
                Scene s = new Scene(wb);
                window.setTitle("Whiteboard");
                window.setScene(s);
                window.show();

                window.setOnCloseRequest(we -> {
                    primaryStage.show();
                });

                primaryStage.hide();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        vb.getChildren().addAll(btnClient,btnDM,btnServer,btnQuit);

        Scene s = new Scene(vb);

        primaryStage.setWidth(400);
        primaryStage.setHeight(220);
        primaryStage.setScene(s);

        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(OSDungeonLauncher.class,args);
    }


}
