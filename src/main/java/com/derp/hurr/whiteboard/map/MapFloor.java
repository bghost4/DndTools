package com.derp.hurr.whiteboard.map;

import com.derp.hurr.whiteboard.Message;
import com.derp.hurr.whiteboard.SendableVisitor;
import com.derp.hurr.whiteboard.messageobjects.Drawable;
import com.derp.hurr.whiteboard.messageobjects.Sendable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

public class MapFloor implements Drawable, Sendable {


    UUID myId;
    String name;
    List<FogArea> fogRegion;

    double layoutX,layoutY;

    public double getLayoutX() {
        return layoutX;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

    /*
        List<Drawable> drawing;
        List<Drawable> tokens;
        */
    List<Object> boundaries;
    byte[] imageData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FogArea> getFogRegion() {
        return fogRegion;
    }

    public void setFogRegion(List<FogArea> fogRegion) {
        this.fogRegion = fogRegion;
    }

    public List<Object> getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(List<Object> boundaries) {
        this.boundaries = boundaries;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public Message asMessage(UUID target, ObjectMapper m) throws JsonProcessingException {
        return Message.createMessage(target,this,m);
    }

    @Override
    public <M, D> M map(SendableVisitor<M, D> mapper, D otherData) {
        return mapper.visit(this,otherData);
    }

    public static class FogArea implements Drawable {

        private MapFloor parent;
        private boolean hidden = false;
        private UUID id;

        @Override
        public UUID getID() {
            return this.id;
        }

        @Override
        public void setID(UUID id) {
            this.id = id;
        }

        @Override
        public Node generateNode() {
            return null;
        }

        @Override
        public boolean hasAnimation() {
            return false;
        }
    }


    public MapFloor() {

    }

    @Override
    public Node generateNode() {
        Pane p = new Pane();
        Image img = new Image(new ByteArrayInputStream(imageData));
        ImageView im = new ImageView(img);

        p.getChildren().add(im);

        p.setId(this.getID().toString());

        return p;
    }

    @Override
    public boolean hasAnimation() {
        return false;
    }

    @Override
    public UUID getID() {
        return this.myId;
    }

    @Override
    public void setID(UUID id) {
        this.myId = id;
    }
}
