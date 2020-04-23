package com.derp.hurr.ui.editors;

import com.derp.hurr.data.Character.NPC;
import com.derp.hurr.data.ContentLibrary;
import com.derp.hurr.data.item.BaseItem;
import com.derp.hurr.data.map.Map;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class ContentLibraryManager extends VBox {
    private ListView<Map> maps = new ListView<>();
    private ListView<NPC> npc = new ListView<>();
    private ListView<? extends BaseItem> items = new ListView<>();

    private ContentLibrary currentLib;

    public ContentLibraryManager(ContentLibrary lib) {
        currentLib = lib;
        maps = new ListView<Map>(FXCollections.observableList(lib.getMaps()));
        Button addMapButton = new Button("Add Map");

        VBox vbMaps = new VBox();
        vbMaps.getChildren().addAll(maps,addMapButton);
        VBox.setVgrow(maps, Priority.ALWAYS);

        TitledPane tpMaps = new TitledPane("Maps",vbMaps);

        this.getChildren().add(tpMaps);

        maps.setOnMouseClicked( me -> {
            if(me.getClickCount() == 2) {
                Map cmap = maps.getSelectionModel().getSelectedItem();
                Stage editorStage = createMapEditor(cmap);
                editorStage.show();

            }
        });

        addMapButton.setOnAction(eh -> {
            TextInputDialog txtMapName = new TextInputDialog();
            txtMapName.setTitle("Map Name?");
            txtMapName.setHeaderText("Please type in a name for your new Map");
            Optional<String> mapname = txtMapName.showAndWait();

            if(mapname.isPresent()) {
                Map nm = Mapbuilder.createNewMap(mapname.get());
                Stage editorStage = createMapEditor(nm);
                editorStage.show();
            } else {
                //Nevermind
            }
        });

    }

    public Stage createMapEditor(Map m) {
        Stage stMapBuilder = new Stage();
        stMapBuilder.setTitle("Map Editor "+m.getMapName());
        Mapbuilder mb = new Mapbuilder(this,m);
        Scene smb = new Scene(mb);
        stMapBuilder.setScene(smb);
        return stMapBuilder;
    }

    private Optional<Integer> getIndexOfMap(Map map) {
        return maps.getItems().stream().filter(m -> m.getID().equals(map.getID())).findFirst().map( m -> Optional.of(maps.getItems().indexOf(m)) ).orElse( Optional.empty());
    }

    public void addMap(Map map) {

        Optional<Integer> idx = getIndexOfMap(map);
        if(idx.isPresent()) {
            int i = idx.get().intValue();
            maps.getItems().set(i,map);
        } else {
            maps.getItems().add(map);
        }

        currentLib.addUpdateMap(map);
    }
}
