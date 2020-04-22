package com.derp.hurr.whiteboard.map;

import com.derp.hurr.whiteboard.messageobjects.Drawable;
import javafx.scene.Node;

import java.util.List;
import java.util.UUID;

public class Map implements Drawable {

    private List<MapFloor> floors;
    private MapFloor currentFloor;
    private String mapName;
    private UUID id;

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

    public List<MapFloor> getFloors() {
        return floors;
    }

    public void setFloors(List<MapFloor> floors) {
        this.floors = floors;
    }

    public MapFloor getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(MapFloor currentFloor) {
        this.currentFloor = currentFloor;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

}
