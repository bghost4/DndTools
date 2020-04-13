package com.derp.hurr.map;

public interface MapItemVisitor<T> {
    T visit(ShapeMarker shapeMarker);
}
