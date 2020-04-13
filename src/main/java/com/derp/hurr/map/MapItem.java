package com.derp.hurr.map;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

public abstract class MapItem extends Group {
    abstract public <T> T visit(MapItemVisitor<T> v);

    //FIXME override toString in subclasses

}
