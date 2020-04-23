package com.derp.hurr.data;

import com.derp.hurr.data.Character.NPC;
import com.derp.hurr.data.item.Item;
import com.derp.hurr.map.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContentLibrary {
    UUID libraryId;

    String name;

    List<Map> maps;
    List<NPC> npcs;
    List<Item> items;

    Path contentPath;

    public ContentLibraryIndex getIndex() {
        var index = new ContentLibraryIndex();

        index.libId = libraryId;
        index.libraryName = name;

        return index;
    }

    public static class ContentLibraryIndex {
        private UUID libId;
        private String libraryName;

        public UUID getLibId() {
            return libId;
        }

        public void setLibId(UUID libId) {
            this.libId = libId;
        }

        public String getLibraryName() {
            return libraryName;
        }

        public void setLibraryName(String libraryName) {
            this.libraryName = libraryName;
        }
    }

    public static ContentLibrary load(Path p) {
        ContentLibrary lib = new ContentLibrary();
        lib.contentPath = p;

        //TODO investigate how "Heavy" these are
        ObjectMapper om = new ObjectMapper();
        om.enableDefaultTyping();

        Path indexPath = p.resolve("index.json");
        Path mapsPath = p.resolve("Maps");
        Path npcPath = p.resolve("Npcs");

        ContentLibraryIndex idx;

        if(!Files.exists(indexPath, LinkOption.NOFOLLOW_LINKS)) {
            //TODO throw no index / bad Library
            return null;
        } else {
            try {
                idx = om.readValue(indexPath.toFile(),ContentLibraryIndex.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }








        return lib;
    }

    public static ContentLibrary createNew(String name, Path destination) {
            ContentLibrary lib = new ContentLibrary();
            lib.libraryId = UUID.randomUUID();
            lib.contentPath = destination;
            lib.maps = new ArrayList<>();
            lib.npcs = new ArrayList<>();
            lib.name = name;

            return lib;
    }

    public void addUpdateMap(Map m) {

    }

    public void addUpdateNPC(NPC npc) {

    }


}
