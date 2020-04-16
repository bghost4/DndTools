package com.derp.hurr.whiteboard;

import javafx.geometry.Point2D;

import java.io.Serializable;

class Point {
    public final double x,y;

    public Point(Point2D src) {
        x = src.getX();
        y = src.getY();
    }

    public Point(double x,double y) {
        this.x = x; this.y = y;
    }

}