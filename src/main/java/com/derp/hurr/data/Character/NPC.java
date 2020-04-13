package com.derp.hurr.data.Character;

import com.derp.hurr.data.item.BaseItem;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class NPC {
    SimpleStringProperty name = new SimpleStringProperty();
    BasicStats stats;
    List<BaseItem> inventory;
}
