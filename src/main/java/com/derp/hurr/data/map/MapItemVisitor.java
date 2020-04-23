package com.derp.hurr.data.map;

public interface MapItemVisitor<T> {
    T visit(ShapeMarker shapeMarker);
}
