package com.derp.hurr.whiteboard.messageobjects;


import com.derp.hurr.whiteboard.Message;
import com.derp.hurr.whiteboard.SendableVisitor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import java.util.*;

public class Figure implements Sendable,Drawable, Styleable {

    private List<PathElement> pathElements;
    private UUID id;
    private String style = "-fx-stroke: BLACK; -fx-stroke-width: 2";

    public Figure(Path p, UUID id) {
        this.id = id;
        pathElements = new ArrayList<>(p.getElements());
    }

    public Figure() { }

    public Figure(List<PathElement> e,UUID id) {
        this.id = id;
        this.pathElements = e;
    }

    public List<PathElement> getPathElements() {
        return pathElements;
    }
    public void setPathElements(List<PathElement> pathElements) {
        this.pathElements = pathElements;
    }

    @Override
    public Node GenerateNode() {
            Path p = new Path();
            p.getElements().addAll(pathElements);
            p.setStyle(getStyle());
            Group g = new Group();
            g.setId(id.toString());
            g.getChildren().add(p);
        return g;
    }

    @Override
    public boolean hasAnimation() {
        return false;
    }

    @Override
    public Message asMessage(UUID target, ObjectMapper m) throws JsonProcessingException {
       return Message.createMessage(target,this,m);
    }

    @Override
    public <M, D> M map(SendableVisitor<M, D> mapper, D otherData) {
        return mapper.visit(this,otherData);
    }

    @Override
    public String getStyle() {
        return this.style;
    }

    @Override
    public void setStyle(String s) {
        this.style = s;
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public void setID(UUID id) {
        this.id = id;
    }
}
