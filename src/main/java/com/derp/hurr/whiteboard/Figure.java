package com.derp.hurr.whiteboard;


import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Figure {

    private List<PathElement> pathElements;
    private UUID id;

    public Figure(Path p, UUID id) {
        this.id = id;
        pathElements = new ArrayList<>(p.getElements());
    }

    public Figure() { }

    public void setId(UUID d) { this.id = d; }

    public Figure(List<PathElement> e,UUID id) {
        this.id = id;
        this.pathElements = e;
    }

    public List<PathElement> getPathElements() {
        return pathElements;
    }

    public void setPathElements(List<PathElement> pathElements) {
        this.pathElements = pathElements;
    }

    public UUID getId() {
        return id;
    }
}
