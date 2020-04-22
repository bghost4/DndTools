package com.derp.hurr.whiteboard.messageobjects;


import com.derp.hurr.whiteboard.ObjectCache;
import javafx.scene.Node;

public interface Drawable extends ID {
    Node generateNode();
    boolean hasAnimation();
    //Set<String> getAnimations();
    //void playAnimation(String animationName);
    //void stopAnimation();
}
