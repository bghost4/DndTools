package com.derp.hurr.whiteboard;

import javafx.application.Platform;
import javafx.geometry.Point2D;
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WhiteBoard extends VBox {

    Socket sock;
    private ObjectOutputStream out;

    private final Pane pane;
    private final TextField txtHostname;
    private final TextField txtPort;
    private final ColorPicker uiColorPicker;
    private final Button btnConnect;

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

        connectPaneControls();

        HBox hb = new HBox();

        hb.getChildren().addAll(new Label("Hostname: "),txtHostname,new Label("Port: "),txtPort,btnConnect);
        this.getChildren().addAll(hb,pane);

    }

    private void connectPaneControls() {

        final ArrayList<Point2D> pnts = new ArrayList<>();

        pane.setOnMousePressed(me ->  {
            pnts.clear();
            pnts.add(new Point2D(me.getX(),me.getY()));
        });

        pane.setOnMouseReleased( me -> {
            pnts.add(new Point2D(me.getX(),me.getY()));
            System.out.println(String.format("%d points",pnts.size()));
            try {
                sendMessage(new ArrayList<>(pnts));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //send message to server
            try {
                sendMessage(pnts);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pane.setOnMouseDragged( me -> {
            Point2D nextPoint = new Point2D(me.getX(),me.getY() );
            Point2D prevPoint = pnts.get(pnts.size()-1);
            pnts.add(nextPoint);
            Line l = new Line(prevPoint.getX(),prevPoint.getY(),nextPoint.getX(),nextPoint.getY());
            pane.getChildren().add(l);
        });

    }

    private void sendMessage(List<Point2D> point2DS) throws IOException {

        ArrayList<Point> pnts = new ArrayList(point2DS.stream().sequential().map(a -> new Point(a)).collect(Collectors.toList()));

        if( sock.isConnected() && out != null ) {
            out.writeObject(pnts);
        }
    }


    private void recieveMessage(List<Point> line) {
        Path p = new Path();

        MoveTo m = new MoveTo(line.get(0).x,line.get(0).y);

        p.setStroke(Color.BLUE);
        p.setStrokeWidth(5.0);

        for(int i = 1; i < line.size(); i++ ) {
            LineTo lt = new LineTo(line.get(i).x,line.get(i).y);
            p.getElements().add(lt);
        }

        pane.getChildren().add(p);

    }

    private void connect() {






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

                                if (o instanceof ArrayList) {
                                    ArrayList<Point> line = (ArrayList<Point>) o;
                                    Platform.runLater(() -> {
                                        recieveMessage(line);
                                    });
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
            listener.start();


    }

}
