package com.derp.hurr.data.map;

import com.derp.hurr.whiteboard.messageobjects.Drawable;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.UUID;

public class Map implements Drawable {

    private ArrayList<MapFloor> floors = new ArrayList<>();
    private MapFloor currentFloor;
    private String mapName;
    private UUID id;

    @Override
    public String toString() {
        return mapName;
    }

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
        return id;
    }

    @Override
    public void setID(UUID id) {
        this.id = id;
    }

    public ArrayList<MapFloor> getFloors() {
        return floors;
    }

    public void setFloors(ArrayList<MapFloor> floors) {
        this.floors = floors;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

}
