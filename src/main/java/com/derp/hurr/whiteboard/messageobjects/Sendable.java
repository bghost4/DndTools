package com.derp.hurr.whiteboard.messageobjects;

import com.derp.hurr.whiteboard.Message;
import com.derp.hurr.whiteboard.SendableVisitor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public interface Sendable {
    Message asMessage(UUID target, ObjectMapper m) throws JsonProcessingException;
    <M,D> M map(SendableVisitor<M,D> mapper, D otherData);
}
