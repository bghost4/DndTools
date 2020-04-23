package com.derp.hurr.data;

import com.derp.hurr.data.Character.NPC;
import com.derp.hurr.data.item.Item;
import com.derp.hurr.data.map.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ContentLibrary {
    UUID libraryId;

    String name;

    List<Map> maps = new ArrayList<>();
    List<NPC> npcs = new ArrayList<>();
    List<Item> items = new ArrayList<>();

    Path contentPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map> getMaps() {
        return maps;
    }

    public void setMaps(List<Map> maps) {
        this.maps = maps;
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

    public void setNpcs(List<NPC> npcs) {
        this.npcs = npcs;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Path getContentPath() {
        return contentPath;
    }

    public void setContentPath(Path contentPath) {
        this.contentPath = contentPath;
    }

    public ContentLibraryIndex getIndex() {
        var index = new ContentLibraryIndex();

        index.libId = libraryId;
        index.libraryName = name;

        return index;
    }

    public static class ContentLibraryIndex {
        private UUID libId;
        private String libraryName;

        private Path libraryPath; //Consider libraryPath as a runtime element not to be serialized

        @Override
        public String toString() {
            return libraryName;
        }

        public Path getLibraryPath() {
            return libraryPath;
        }

        public void setLibraryPath(Path libraryPath) {
            this.libraryPath = libraryPath;
        }

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
                idx.setLibraryPath(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!Files.exists(mapsPath,LinkOption.NOFOLLOW_LINKS)) {

        } else {
            try {
                Files.newDirectoryStream(mapsPath).forEach(cp -> {
                    try {
                        Map cpm = om.readValue(cp.toFile(),Map.class);
                        lib.getMaps().add(cpm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
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

    public void save() throws IOException {
        System.out.println("Called save on ContentLibrary: "+this.name);

        var index = getIndex();

        ObjectMapper om = new ObjectMapper();
        om.enableDefaultTyping();

        if(!Files.exists(contentPath)) {
            Files.createDirectories(contentPath);
        }

        om.writeValue(contentPath.resolve("index.json").toFile(),index);
        Path mapsPath = contentPath.resolve("Maps");
        if(!Files.exists(mapsPath)) { Files.createDirectories(mapsPath); }
        maps.forEach( m -> {
            Path tmPath = mapsPath.resolve(m.getID().toString());
            try {
                om.writeValue(tmPath.toFile(),m);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void addUpdateMap(Map map) {
        Optional<Integer> oidx = maps.stream().filter(m -> m.getID().equals(map.getID())).findFirst().map(m -> Optional.of(maps.indexOf(m))).orElse( Optional.empty());

        if(oidx.isPresent()) {
            maps.set(oidx.get().intValue(),map);
        } else {
            maps.add(map);
        }

        ObjectMapper om = new ObjectMapper();
        om.enableDefaultTyping();

        try {
            om.writeValue(contentPath.resolve("Maps").resolve(map.getID().toString()).toFile(),map);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void addUpdateNPC(NPC npc) {

    }



}
