package com.derp.hurr.map;

import com.derp.hurr.data.DataItem;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.UUID;

public class ShapeMarker extends MapItem {

    Shape s;
    Paint p;
    DataItem item;

    private final UUID identifier;

    @Override
    public UUID getIdentifier() {
        return identifier;
    }

    @Override
    public <T> T visit(MapItemVisitor<T> v) {
        return v.visit(this);
    }

    public enum MIShape { Circle, Square, Diamond, Triangle, Hexagon, Octagon, Arrow }

    public ShapeMarker(MIShape shape, Paint color, DataItem item, UUID id) {

        if( id == null ) { identifier = UUID.randomUUID(); } else { identifier = id; }

        p = color;
        this.item = item;

        switch(shape) {
            case Circle:
                s = new Circle(50.0,50.0,50.0);
                break;
            case Square:
                s = new Rectangle(0,0,100,100);
                break;
            case Diamond:
                Polygon poly = new Polygon();

                poly.getPoints().addAll(25.0, 50.0,
                        50.0, 0.0,
                        75.0, 50.0,
                        50.0,100.0);

                s = poly;
                break;
            case Triangle:
                Polygon ptri = new Polygon();
                ptri.getPoints().addAll(50.0,0.0,
                        100.0,100.0,
                        0.0,100.0);
                s = ptri;
                break;
            case Hexagon:
                //TODO
            case Octagon:
            case Arrow:

            default:
                s = new Circle(50.0, 50.0, 50.0);
                break;
        }

        s.setStroke(p);
        s.setStrokeWidth(5.0);
        s.setFill(Color.TRANSPARENT);

        this.getChildren().add(s);

    }


}
