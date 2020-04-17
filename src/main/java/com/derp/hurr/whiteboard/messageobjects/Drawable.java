package com.derp.hurr.whiteboard.messageobjects;


import javafx.scene.Node;

public interface Drawable extends ID {
    Node GenerateNode();
    boolean hasAnimation();
    //Set<String> getAnimations();
    //void playAnimation(String animationName);
    //void stopAnimation();
}
