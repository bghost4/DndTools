package com.derp.hurr.map;


import com.derp.hurr.whiteboard.ObjectCache;
import com.derp.hurr.whiteboard.map.Map;
import com.derp.hurr.whiteboard.map.MapFloor;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.*;

import javafx.scene.image.Image;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class Mapbuilder extends VBox {

    ListView<MapFloor> lstLayers;
    ScrollPane scroll;
    Pane pane;
    Spinner<Double> spnZoom;
    ObjectCache cache = new ObjectCache();

    Group zoomGroup;

    Map map = new Map();

    public Mapbuilder() {

        lstLayers = new ListView<>();
        zoomGroup = new Group();
        pane = new Pane();
        zoomGroup.getChildren().add(pane);
        scroll = new ScrollPane(zoomGroup);
        spnZoom = new Spinner<>(.1,1.0,0.25,.1);
        pane.scaleXProperty().bind(spnZoom.valueProperty());
        pane.scaleYProperty().bind(spnZoom.valueProperty());

        VBox vbMapView = new VBox();
        HBox hbZoom = new HBox();

        hbZoom.getChildren().addAll(new Label("Zoom"),spnZoom);
        vbMapView.getChildren().addAll(hbZoom,scroll);

        VBox vbLayers = new VBox();
        HBox hbLayerButtons = new HBox();

        Button btnAdd,btnRemove,btnMoveUp,btnMoveDown,btnHide,btnAdjust;

        btnAdd = new Button("+");
        btnRemove = new Button("X");
        btnMoveUp = new Button("Up");
        btnMoveDown = new Button("Down");
        btnHide = new Button("Hide");
        btnAdjust = new Button("Adjust");

        hbLayerButtons.getChildren().addAll(btnAdd,btnRemove,btnMoveUp,btnMoveDown,btnHide,btnAdjust);
        vbLayers.getChildren().addAll(lstLayers,hbLayerButtons);

        btnAdd.setOnAction(this::actionAddLayer);
        btnRemove.setOnAction(this::removeSelectedLayer);
        btnAdjust.setOnAction(this::actionAdjust);
        btnHide.setOnAction(this::hideLayer);

        SplitPane s = new SplitPane();
        s.getItems().addAll(vbMapView,vbLayers);

        this.getChildren().add(s);

        lstLayers.setCellFactory( i -> {
            return new ListCell<MapFloor>() {
                @Override
                protected void updateItem(MapFloor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setText(item.getName());
                        setGraphic(null);
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
        });

        lstLayers.setContextMenu(layerContextMenu());

    }

    private void hideLayer(ActionEvent actionEvent) {
        MapFloor l = lstLayers.getSelectionModel().getSelectedItem();
        if( l != null) {
            //FIXME
            //l.setVisible(!l.isVisible());
        }
    }

    private ContextMenu layerContextMenu() {

        ContextMenu ctx = new ContextMenu();

        MenuItem miRename = new MenuItem("Rename Layer");
        MenuItem addNPC = new MenuItem("Add NPC to Layer");
        MenuItem addItem = new MenuItem( "Add Other BaseItem");
        MenuItem addShapeMarker = new MenuItem("Add Shape Marker");

            miRename.setOnAction(this::renameLayer);

            addShapeMarker.setOnAction(eh -> {
                if( lstLayers.getSelectionModel().getSelectedItem() != null) {
                    MapFloor layer = lstLayers.getSelectionModel().getSelectedItem();
                    //layer.addMapItem(new ShapeMarker(ShapeMarker.MIShape.Circle, Color.BLUEVIOLET,null, UUID.randomUUID()),null);
                    System.out.println("Added Shape");
                } else {
                    System.out.println("Could not get a Lock on the Layer");
                }
            });


        ctx.getItems().addAll(miRename,addNPC,addItem,addShapeMarker);
        return ctx;
    }

    private void removeSelectedLayer(ActionEvent actionEvent) {

        MapFloor l = lstLayers.getSelectionModel().getSelectedItem();
        if( l != null) {
            removeLayer(l);
        }

    }

    public void actionAddLayer(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Image File");

        //TODO reset this later
        //fc.setInitialDirectory(Paths.get("/home/dmartin/dndmap").toFile());

        File f = fc.showOpenDialog(null);

        if(f != null) {
            addLayer(f);
        }

    }

    public void renameLayer(ActionEvent evt) {
        MapFloor active = lstLayers.getSelectionModel().getSelectedItem();
        if(active != null) {
            TextInputDialog dLayerName = new TextInputDialog(active.getName());
            dLayerName.setTitle("Rename Layer");
            String newname = dLayerName.showAndWait().orElse(active.getName());
            active.setName(newname);
        }

    }

    public void addLayer(File f) {
        MapFloor i = new MapFloor(); //new Image(s),"New Layer");
        i.setName( " New Floor ");
        i.setID(UUID.randomUUID());
        try {
            i.setImageData(Files.readAllBytes(f.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cache.putObject(i);

        lstLayers.getItems().add(i);

        //FIXME
        pane.getChildren().add(cache.getNode(i.getID()));

    }

    public void removeLayer(MapFloor l) {
        lstLayers.getItems().remove(l);

        //TODO pane.getChildren().remove(l);
    }

    public void actionAdjust(ActionEvent e) {
        MapFloor l = lstLayers.getSelectionModel().getSelectedItem();
        if( l != null) {
            //TODO FIXME
            //l.setOpacity(0.4);  //TODO make adjustable
            AdjustDialog ad = new AdjustDialog(l);
            ad.showAndWait();
        }
    }


}
