package com.derp.hurr.whiteboard;

import com.derp.hurr.map.Map;
import com.derp.hurr.whiteboard.messageobjects.Drawable;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.UUID;

public class ObjectCache {
    HashMap<UUID, Node> drawables = new HashMap<>();
    HashMap<UUID, Drawable> dwObjects = new HashMap<>();
    HashMap<UUID, Map> mapCache = new HashMap<>();

    public Node getNode(UUID id) {
        if(dwObjects.containsKey(id)) {
            return drawables.computeIfAbsent(id,(d) -> dwObjects.get(d).generateNode());
        } else {
            //NO Object
            return null;
        }
    }

    public void putObject(Drawable d) {
        dwObjects.put(d.getID(),d);
    }



}
