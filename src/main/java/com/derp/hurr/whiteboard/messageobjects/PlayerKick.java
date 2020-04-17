package com.derp.hurr.whiteboard.messageobjects;

import com.derp.hurr.whiteboard.Message;
import com.derp.hurr.whiteboard.SendableVisitor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class PlayerKick implements Sendable {

    String reason;

    public PlayerKick() { }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public Message asMessage(UUID target, ObjectMapper m) throws JsonProcessingException {
        return Message.createMessage(target,this,m);
    }

    @Override
    public <M, D> M map(SendableVisitor<M, D> mapper, D otherData) {
        return mapper.visit(this,otherData);
    }

}
