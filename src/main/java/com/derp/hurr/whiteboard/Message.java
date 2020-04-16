package com.derp.hurr.whiteboard;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    enum Type { Create, Update, Delete }

    public final Message.Type type;
    public final byte[] data;
    //public final String classname;



    public Message(Message.Type type,byte[] data) {
        this.type = type;
        this.data = data;
    }

    public Type getType() { return type; }
    public byte[] getData() { return data; }

}
