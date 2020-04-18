package com.derp.hurr.whiteboard.map;

import com.derp.hurr.whiteboard.messageobjects.Drawable;
import com.derp.hurr.whiteboard.messageobjects.ID;
import javafx.scene.Node;

import java.util.List;
import java.util.UUID;

public class MapFloor implements Drawable {


    public static class FogArea implements ID {

        private MapFloor parent;
        private boolean hidden = false;

        @Override
        public UUID getID() {
            return null;
        }

        @Override
        public void setID(UUID id) {

        }
    }

    UUID myId;
    List<FogArea> fogRegion;
    List<Drawable> drawing;
    List<Drawable> tokens;
    List<Object> boundaries;

    public MapFloor() {

    }

    @Override
    public Node generateNode() {
        return null;
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
