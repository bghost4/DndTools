module OSDungeon.main {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.prefs;

    //Appears to be needed by jackson
    requires java.sql;

    exports com.derp.hurr.ui;
    exports com.derp.hurr.data;
    exports com.derp.hurr.data.map;

}