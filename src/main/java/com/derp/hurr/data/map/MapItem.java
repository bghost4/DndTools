package com.derp.hurr.data.map;

import com.derp.hurr.data.DataItem;
import javafx.scene.Group;

public abstract class MapItem extends Group implements DataItem {

    abstract public <T> T visit(MapItemVisitor<T> v);

    //FIXME override toString in subclasses

}
