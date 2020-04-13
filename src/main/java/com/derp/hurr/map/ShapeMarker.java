package com.derp.hurr.map;

import com.derp.hurr.data.DataItem;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ShapeMarker extends MapItem {

    Shape s;
    Paint p;
    DataItem item;
    Layer lref;
    Group gref;

    @Override
    public <T> T visit(MapItemVisitor<T> v) {
        return v.visit(this);
    }

    public enum MIShape { Circle, Square, Diamond, Triangle, Hexagon, Octagon, Arrow }

    public void setLayerRef(Layer l) { lref = l; }

    public void setGref(Group g) { gref = g; }

    public ShapeMarker(MIShape shape, Paint color, DataItem item) {

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

                poly.getPoints().addAll(new Double[] {
                        25.0, 50.0,
                        50.0, 0.0,
                        75.0, 50.0,
                        50.0,100.0
                });

                s = poly;
                break;
            case Triangle:
                Polygon ptri = new Polygon();
                ptri.getPoints().addAll(new Double[] {
                        50.0,0.0,
                        100.0,100.0,
                        0.0,100.0
                });
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

//        s.setOnMouseDragged(me -> {
//            System.out.println("Mouse Dragged");
//
//            if(lref != null) {
//
//            }
//
//        });


    }


}
