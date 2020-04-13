package com.derp.hurr.data;

import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class NPC {
    SimpleStringProperty name = new SimpleStringProperty();
    BasicStats stats;
    List<Item> inventory;
}
