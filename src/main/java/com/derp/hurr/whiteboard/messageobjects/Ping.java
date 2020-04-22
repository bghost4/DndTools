package com.derp.hurr.whiteboard.messageobjects;

import com.derp.hurr.whiteboard.Message;
import com.derp.hurr.whiteboard.SendableVisitor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.paint.Color;

import java.util.UUID;

public class Ping implements Sendable {

    private double xLocation,yLocation;
    private String style;
    private Color color;

    public Ping() {

    }

    public Ping(double x, double y) {
        setLocation(x,y);
    }

    public void setLocation(double x, double y) {
        xLocation = x;
        yLocation = y;
    }

    public double getxLocation() {
        return xLocation;
    }

    public void setxLocation(double xLocation) {
        this.xLocation = xLocation;
    }

    public double getyLocation() {
        return yLocation;
    }

    public void setyLocation(double yLocation) {
        this.yLocation = yLocation;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public Message asMessage(UUID target, ObjectMapper om) throws JsonProcessingException {
        return Message.createMessage(target,this,om);
    }

    @Override
    public <M, D> M map(SendableVisitor<M, D> mapper, D otherData) {
        return mapper.visit(this,otherData);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
