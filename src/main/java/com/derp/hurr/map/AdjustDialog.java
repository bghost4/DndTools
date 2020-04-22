package com.derp.hurr.map;

import com.derp.hurr.whiteboard.map.MapFloor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class AdjustDialog extends Dialog<Void> {

    public AdjustDialog(MapFloor l) {

        Spinner<Double> offX,offY,opacity;
        Button btnCW,btnRCCW;

        offX = new Spinner<>(-Double.MAX_VALUE,Double.MAX_VALUE,l.getLayoutX());
        offY = new Spinner<>(-Double.MAX_VALUE,Double.MAX_VALUE,l.getLayoutY());
        opacity = new Spinner<>(0.0,1.0,l.getLayoutY(),0.1);

        this.setTitle("Adjust Layer: "+l.getName());

        GridPane gp = new GridPane();

        gp.add(new Label("X Placement"),0,0);
        gp.add(offX,1,0);
        gp.add(new Label("Y Placement"),0,1);
        gp.add(offY,1,1);
        gp.add(new Label("Layer Opacity"),0,2);
        gp.add(opacity,1,2);


        //ew
        offX.getValueFactory().valueProperty().addListener( (ob,ov,nv) -> l.setLayoutX(nv) );
        offY.getValueFactory().valueProperty().addListener( (ob,ov,nv) -> l.setLayoutY(nv) );
        opacity.valueProperty().addListener((ob,ov,nv) -> l.setOpacity(nv));

        this.getDialogPane().setContent(gp);
        this.getDialogPane().getButtonTypes().add(ButtonType.OK);

        this.setResultConverter(btn -> { l.setOpacity(1.0); return null; } );

    }

}
