package com.derp.hurr.whiteboard;

import com.derp.hurr.whiteboard.map.MapFloor;
import com.derp.hurr.whiteboard.messageobjects.Figure;
import com.derp.hurr.whiteboard.messageobjects.Ping;
import com.derp.hurr.whiteboard.messageobjects.PlayerKick;
import com.derp.hurr.whiteboard.messageobjects.SetPlayerID;

public interface SendableVisitor<M,D> {
    M visit(Ping ping, D otherData);
    M visit(Figure fig, D otherData);
    M visit(PlayerKick playerKick, D otherData);
    M visit(SetPlayerID setPlayerID, D otherData);
    M visit(MapFloor mf, D otherData);
}
