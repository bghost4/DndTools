package com.derp.hurr.whiteboard;

import com.derp.hurr.whiteboard.messageobjects.Sendable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    public final UUID target;
    public final byte[] data;
    public final String classname;
    private UUID source;


    public Message(UUID tgt,String cname, byte[] object) {
        this.target = tgt;
        this.classname = cname;
        this.data = object;
    }

    public UUID getSource() {
        return source;
    }

    public String getClassName() { return classname; }

    public void setSource(UUID source) {
        this.source = source;
    }

    public byte[] getData() { return data; }

    public static Message createMessage(UUID target, Sendable o, ObjectMapper mapper) throws JsonProcessingException {
        byte[] dat = mapper.writeValueAsBytes(o);

        return new Message(target,o.getClass().getName(),dat);
    }

}
