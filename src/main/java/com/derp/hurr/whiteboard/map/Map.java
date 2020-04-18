package com.derp.hurr.whiteboard.map;

import com.derp.hurr.whiteboard.messageobjects.Drawable;
import javafx.scene.Node;

import java.util.List;
import java.util.UUID;

public class Map implements Drawable {


    private List<MapFloor> floors;
    private MapFloor currentFloor;

    @Override
    public Node generateNode() {
        return currentFloor.generateNode();
    }

    @Override
    public boolean hasAnimation() {
        return false;
    }

    @Override
    public UUID getID() {
        return null;
    }

    @Override
    public void setID(UUID id) {

    }
}
