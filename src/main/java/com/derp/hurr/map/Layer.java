package com.derp.hurr.map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


/* Layer Messages *
 * AddMapItem   <MapItem>
 * SetMapItemLocation   <X,Y>
 * PlayerPing <X,Y>
 * RevalFogArea <Area UUID>
 * PlayerDraw   <List[Drawables]>
 * AllowMovement < MapItemId,List<Player> >
 */

public class Layer extends Pane {
    private ImageView base;
    private Canvas fogLayer;
    private GraphicsContext fogGC;


    private SimpleStringProperty name = new SimpleStringProperty();

    private MapItem dragItem = null;
    private boolean isDragging = false;

    //TODO order is essentially the draw order of the items;
    private List<MapItem> items = new ArrayList<>();


    public Layer(Image base,String name) {

        this.base = new ImageView(base);
        this.name.set(name);
        this.fogLayer = new Canvas(base.getWidth(),base.getHeight());
        fogGC = fogLayer.getGraphicsContext2D();
        fogGC.setFill(Color.GRAY);
        fogGC.fillRect(0,0,base.getWidth(),base.getHeight());
        fogLayer.setOpacity(0.75);

        this.getChildren().add(this.base);
        this.getChildren().add(this.fogLayer);

    }

    private void dragMove(MouseEvent me) {
        Node n = (Node)me.getSource();
        n.setTranslateX( n.getTranslateX() + me.getX() );
        n.setTranslateY( n.getTranslateY() + me.getY() );
        //TODO send update to clients?
    }

    public void relayer() {
        this.getChildren().removeAll();
        this.getChildren().add(base);
        this.getChildren().addAll(items);
        this.layout();
    }

    public void addMapItem(MapItem i, Point2D location) {

        items.add(i);

        //TODO set Location

        double x = base.getImage().getWidth() / 2.0 + 50.0;
        double y = base.getImage().getHeight() / 2.0 + 50.0;

        i.setLayoutX(x);
        i.setLayoutY(y);

        getChildren().add(i);

        i.setOnMouseDragged(this::dragMove);


        System.out.println("BaseItem added to "+getName());
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() { return name; }

}
