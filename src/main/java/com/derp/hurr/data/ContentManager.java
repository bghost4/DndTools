package com.derp.hurr.data;

import com.derp.hurr.data.ContentLibrary.ContentLibraryIndex;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;


public class ContentManager extends VBox {

    ListView<ContentLibraryIndex> libraries;

    Path pBase,pContent,pChars;

    public ContentManager() {

        validateContentLibrary();


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
            libraries.getItems().add(lib.getIndex());
        } else {
            //TODO Library Exists
        }
    }

    public void removeLibrary(String name) {

    }

    public void scanLibraries() {
        libraries.getItems().clear();



    }

}
