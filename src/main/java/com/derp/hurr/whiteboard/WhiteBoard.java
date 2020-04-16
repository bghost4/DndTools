package com.derp.hurr.whiteboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WhiteBoard extends VBox {

    Socket sock;
    private ObjectOutputStream out;

    private final Pane pane;
    private final TextField txtHostname;
    private final TextField txtPort;
    private final ColorPicker uiColorPicker;
    private final Button btnConnect;
    private final ObjectMapper mapper;

    private Path path;

    private final List<Path> figures = new ArrayList<>();

    enum Mode { Select, FreeHand, Line }

    private Mode mode;

    public WhiteBoard() {

        pane = new Pane();
        pane.setPrefSize(640,480);
        pane.setMaxSize(640,480);
        pane.setMinSize(640,480);

        txtHostname = new TextField();
        txtHostname.setPromptText("Hostname");
        txtPort = new TextField();
        txtPort.setText("8888");

        uiColorPicker = new ColorPicker();

        btnConnect = new Button("Connect");
        btnConnect.setOnAction(ae -> {
            connect();
        });

        mapper = new ObjectMapper();
        mapper.enableDefaultTyping();

        connectPaneControls();

        HBox hb = new HBox();

        hb.getChildren().addAll(new Label("Hostname: "),txtHostname,new Label("Port: "),txtPort,btnConnect);
        this.getChildren().addAll(hb,pane);

    }

    private void connectPaneControls() {

        pane.setOnMousePressed(me ->  {
            path = new Path();
            path.setId(UUID.randomUUID().toString());
            path.setStrokeWidth(2.0);
            path.setStroke(Color.BLACK);
            path.getElements().add(new MoveTo(me.getX(),me.getY()));
            pane.getChildren().add(new Group(path));
        });

        pane.setOnMouseReleased( me -> {
            path.getElements().add(new LineTo(me.getX(),me.getY()));
            //System.out.println(String.format("%d points",pnts.size()));
            try {
                sendMessage(new Figure(path, UUID.fromString(path.getId())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pane.setOnMouseDragged( me -> {
            path.getElements().add(new LineTo(me.getX(),me.getY()));
        });

    }

    private void sendMessage(Figure f) throws IOException {

        //Message m = new Message();

        if( sock.isConnected() && out != null ) {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            mapper.writeValue(bao,f);
            Message m = new Message(Message.Type.Create,bao.toByteArray());
            out.writeObject(m);
        }
    }


    private void recieveMessage(Message m) {
        if(m.getType() == Message.Type.Create) {
            ByteArrayInputStream bais = new ByteArrayInputStream(m.getData());
            try {
                Figure f = mapper.readValue(bais,Figure.class);
                Path p = new Path();
                p.setStroke(Color.BLUE);
                p.setStrokeWidth(2.0);
                p.getElements().addAll(f.getPathElements());
                p.setId(f.getId().toString());

                pane.getChildren().add(new Group(p));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void connect() {

            if(sock != null && sock.isConnected()) {
                try {
                    out.close();
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    System.out.println("Reading Listener Started");
                    ObjectInputStream in = null;
                    boolean running = true;
                    try (Socket socket = new Socket(txtHostname.getText(),Integer.parseInt(txtPort.getText())) ) {
                        sock = socket;
                        in = new ObjectInputStream(socket.getInputStream());
                        out = new ObjectOutputStream(sock.getOutputStream());
                        while (running && socket.isConnected()) {
                            try {
                                Object o = in.readObject();

                                System.out.println("Got An Object");

                                if (o instanceof Message) {
                                    Platform.runLater(() -> {
                                        recieveMessage((Message)o);
                                    });
                                } else {
                                    System.err.println("Object was Not Message");
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                running = false;
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                running = false;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        sock.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            };

            Thread listener = new Thread(r);
            listener.setDaemon(true);
            listener.start();

    }

}
