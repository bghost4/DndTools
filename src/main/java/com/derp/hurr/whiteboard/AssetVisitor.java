package com.derp.hurr.whiteboard;

public interface AssetVisitor<M,O> {

    M visit(MapItemAsset mapItemAsset, O data);
}
