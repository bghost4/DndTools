package com.derp.hurr.whiteboard.messageobjects;

import com.derp.hurr.whiteboard.Message;
import com.derp.hurr.whiteboard.SendableVisitor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class SetPlayerID implements Sendable {

    public UUID playerID;

    public SetPlayerID() { }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    @Override
    public Message asMessage(UUID target, ObjectMapper m) throws JsonProcessingException {
        return Message.createMessage(playerID,this,m);
    }

    @Override
    public <M, D> M map(SendableVisitor<M, D> mapper, D otherData) {
        return mapper.visit(this,otherData);
    }

}
