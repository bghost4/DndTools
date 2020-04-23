package com.derp.hurr.ui.editors;

import com.derp.hurr.data.ContentLibrary;
import com.derp.hurr.data.ContentLibrary.ContentLibraryIndex;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.prefs.Preferences;


public class ContentManager extends VBox {

    ListView<ContentLibraryIndex> libraries = new ListView<>();

    Path pBase,pContent,pChars;

    public ContentManager() {

        validateContentLibrary();

        libraries.setOnMouseClicked( me -> {
            if(me.getClickCount() == 2) {
                var item = libraries.getSelectionModel().getSelectedItem();
                if( item != null ) {
                    Stage stLibEditor = new Stage();
                    stLibEditor.setTitle("Library Editor: "+item.getLibraryName());
                    ContentLibraryManager mgr = new ContentLibraryManager(ContentLibrary.load(item.getLibraryPath()));
                    Scene s = new Scene(mgr);
                    stLibEditor.setScene(s);
                    stLibEditor.show();
                }
            } else if( me.getClickCount() == 1) {
                var item = libraries.getSelectionModel().getSelectedItem();
                System.out.println("Library Path: "+item.getLibraryPath());
            }
        });

        Button btnCreateNew = new Button("Create New Library");
        Button btnImportLibrary = new Button("Import Library");
        Button btnExportLibrary = new Button("Export Library");

        VBox.setVgrow(libraries, Priority.ALWAYS);

        this.getChildren().addAll(libraries,btnCreateNew,btnImportLibrary,btnExportLibrary);

        btnCreateNew.setOnAction(eh -> {
            TextInputDialog dlgname = new TextInputDialog();
            dlgname.setTitle("Name your new Library");
            dlgname.setHeaderText("Library Name:");
            Optional<String> olibname = dlgname.showAndWait();
            olibname.ifPresent(nm -> createLibrary(nm) );
        });

        scanLibraries();

    }

    public void validateContentLibrary() {
        var prefs = Preferences.userRoot().node(this.getClass().getName());
        pBase = Paths.get(System.getProperty("user.home")).resolve("OSDungeon");
        pContent = pBase.resolve("ContentLibraries");
        pChars = pBase.resolve("Characters");

        prefs.get("ContentLocation",pContent.toString());
        try {
            if(!Files.exists(pBase)) {
                Files.createDirectory(pBase);
            }
            if(!Files.exists(pContent, LinkOption.NOFOLLOW_LINKS)) {
                Files.createDirectory(pContent);
            }

            if(!Files.exists(pChars)) {
                Files.createDirectory(pChars);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void createLibrary(String name) {
        if(!Files.exists(pContent.resolve(name))) {
            var lib = ContentLibrary.createNew(name, pContent.resolve(name));
            try {
                lib.setContentPath(pContent.resolve(name));
                lib.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ContentLibraryIndex idx = lib.getIndex();
            idx.setLibraryPath(pContent.resolve(name));
            libraries.getItems().add(lib.getIndex());
        } else {
            //TODO Library Exists
        }
    }

    public void removeLibrary(String name) {

    }

    public void scanLibraries() {

        ObjectMapper om = new ObjectMapper();
        om.enableDefaultTyping();

        libraries.getItems().clear();

        try {
            Files.newDirectoryStream(pContent).forEach(cp -> {
                try {
                    System.out.println("Trying to read "+cp.resolve("index.json").toString());
                    ContentLibraryIndex cli = om.readValue(cp.resolve("index.json").toFile(),ContentLibraryIndex.class);
                    cli.setLibraryPath(cp);
                    libraries.getItems().add(cli);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
