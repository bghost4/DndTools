package com.derp.hurr.map;

import com.derp.hurr.data.DataItem;
import javafx.scene.Group;
import javafx.scene.layout.Pane;

import java.util.UUID;

public abstract class MapItem extends Group implements DataItem {

    abstract public <T> T visit(MapItemVisitor<T> v);

    //FIXME override toString in subclasses

}
