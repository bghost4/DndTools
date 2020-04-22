package com.derp.hurr.whiteboard;

import com.derp.hurr.whiteboard.map.MapFloor;
import com.derp.hurr.whiteboard.messageobjects.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WhiteBoardInterface extends VBox {

    Socket sock;
    private ObjectOutputStream out;

    private final ScrollPane scrollPane;
    private final Pane pane;
    private final TextField txtHostname;
    private final TextField txtPort;
    private final ColorPicker uiColorPicker;
    private final Button btnConnect;
    private final ObjectMapper mapper;

    private final Map<UUID, Drawable> drawables;

    private final ToggleGroup drawCntrlsGroup;

    private UUID currentScreen;
    private UUID playerID;


    private Path path;
    private SendableVisitor<Void, Void> defaultSenderVisitor;

    enum Mode { Select, FreeHand, Line }

    public WhiteBoardInterface() {

        drawables = new HashMap<>();
        defaultSenderVisitor = new Reciever();

        pane = new Pane();
        pane.setMinSize(640,480);
        pane.setPrefSize(640,480);
        scrollPane = new ScrollPane(pane);

        scrollPane.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

        txtHostname = new TextField();
        txtHostname.setPromptText("Hostname");
        txtPort = new TextField();
        txtPort.setText("8888");

        uiColorPicker = new ColorPicker();

        btnConnect = new Button("Connect");
        btnConnect.setOnAction(ae -> {
            connect();
        });

        ToggleButton tglSelect,tglFreeHand,tglLine;

        HBox tbHB = new HBox();
        tglSelect = new ToggleButton("S");
            tglSelect.setTooltip(new Tooltip("Select Items"));
        tglFreeHand = new ToggleButton("F");
            tglFreeHand.setTooltip(new Tooltip("Freehand Draw"));
        tglLine = new ToggleButton("L");
            tglLine.setTooltip(new Tooltip("Draw Lines"));

        drawCntrlsGroup = new ToggleGroup();
        drawCntrlsGroup.getToggles().addAll(tglSelect,tglFreeHand,tglLine);

        tbHB.getChildren().addAll(tglSelect,tglFreeHand,tglLine);

        mapper = new ObjectMapper();
        mapper.enableDefaultTyping();

        connectPaneControls();

        HBox hb = new HBox();
        hb.getChildren().addAll(new Label("Hostname: "),txtHostname,new Label("Port: "),txtPort,btnConnect);
        this.getChildren().addAll(hb,scrollPane,tbHB);

    }

    private void connectPaneControls() {

        pane.setOnMousePressed(me ->  {

            if(me.isControlDown()) {
                Ping p = new Ping(me.getX(),me.getY());
                try {
                    sendMessage(p.asMessage(UUID.randomUUID(),mapper));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            path = new Path();
            path.setId(UUID.randomUUID().toString());
            path.setStrokeWidth(2.0);
            path.setStroke(Color.BLACK);
            path.getElements().add(new MoveTo(me.getX(),me.getY()));
            pane.getChildren().add(new Group(path));
        });

        pane.setOnMouseReleased( me -> {

            if(me.isControlDown()) {
                return;
            }

            path.getElements().add(new LineTo(me.getX(),me.getY()));
            //System.out.println(String.format("%d points",pnts.size()));
            try {
                //TODO replace UUID with a Valid UUID?
                sendMessage(new Figure(path, UUID.fromString(path.getId())).asMessage(UUID.randomUUID(),mapper));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pane.setOnMouseDragged( me -> {
            if(me.isControlDown()) { return; }
            path.getElements().add(new LineTo(me.getX(),me.getY()));
        });

    }

    private void sendMessage(Message m) throws IOException {
        if( m == null) { System.err.println("The Message was Null"); }
        if( playerID == null) { System.err.println("The PlayerID Was Null"); }

        m.setSource(playerID);
        if( sock.isConnected() && out != null ) {
            out.writeObject(m);
        }
    }

    private void recieveMessage(Message m) {

        try {
            Class<?> objectClass = Class.forName(m.getClassName());
            System.out.println("Message Type Class: "+m.getClassName());
            System.out.println("Data Contents: "+new String(m.getData()));

            Object s = mapper.readValue(m.getData(),objectClass);
            if(s instanceof Sendable) {
                Sendable sendable = (Sendable)s;
                sendable.map(defaultSenderVisitor,null);
            }
        } catch (ClassNotFoundException e) {
            //Protocol Error
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if(m.getType() == Message.Type.Create) {
//            ByteArrayInputStream bais = new ByteArrayInputStream(m.getData());
//            try {
//                Figure f = mapper.readValue(bais,Figure.class);
//                Path p = new Path();
//                p.setStroke(Color.BLUE);
//                p.setStrokeWidth(2.0);
//                p.getElements().addAll(f.getPathElements());
//                p.setId(f.getId().toString());
//
//                pane.getChildren().add(new Group(p));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
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
                    } catch(ConnectException e )  {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Connection Problem:");
                            alert.setContentText(e.getStackTrace().toString());
                            alert.showAndWait();
                        });

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

    private class Reciever implements SendableVisitor<Void,Void> {

        @Override
        public Void visit(Ping ping, Void otherData) {

            Circle c = new Circle();
            c.setCenterX(ping.getxLocation());
            c.setCenterY(ping.getyLocation());
            c.setRadius(1);

            c.setStroke(Color.BLUE);
            c.setStrokeWidth(5.0);
            c.setFill(Color.TRANSPARENT);

            Timeline tl = new Timeline();
            tl.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(1000),new KeyValue(c.radiusProperty(),50.0)),
                new KeyFrame(Duration.millis(2000),new KeyValue(c.radiusProperty(),5.0))
            );

            //TODO put in own thread?
            tl.setOnFinished(eh -> {
                pane.getChildren().remove(c);
            });

            pane.getChildren().add(c);
            tl.play();

            return null;
        }

        @Override
        public Void visit(Figure fig, Void otherData) {
            Node n = fig.generateNode();
            pane.getChildren().add(n);
            return null;
        }

        @Override
        public Void visit(PlayerKick playerKick, Void otherData) {
            //PlayerKick is a targeted Object if you get this. exit the game
            return null;
        }

        @Override
        public Void visit(SetPlayerID setPlayerID, Void otherData) {
            System.out.println("PlayerID is Set");
            WhiteBoardInterface.this.playerID = setPlayerID.getPlayerID();
            return null;
        }

        @Override
        public Void visit(MapFloor mf, Void otherData) {

            Node n = mf.generateNode();
            pane.getChildren().add(n);
            n.toBack();


            return null;
        }
    }

}
