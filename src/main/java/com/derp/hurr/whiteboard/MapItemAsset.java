package com.derp.hurr.whiteboard;

public class MapItemAsset extends Asset {

    <M,O> M visit(AssetVisitor<M,O> v,O data) {
        return v.visit(this,data);
    }

}
