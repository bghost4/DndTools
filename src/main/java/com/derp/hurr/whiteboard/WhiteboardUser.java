package com.derp.hurr.whiteboard;

import com.derp.hurr.whiteboard.messageobjects.SetPlayerID;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class WhiteboardUser implements Runnable {

    private Socket s;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private WhiteboardServer srv;

    public final UUID id = UUID.randomUUID();


    public WhiteboardUser(Socket s,WhiteboardServer srv) {
        this.srv = srv;
        this.s = s;

    }

    public void giveMessage(Message pnts) {
        try {
            out.writeObject(pnts);
        } catch(Exception e) {
            srv.removeUser(this,e);
        }
    }

    @Override
    public void run() {
        System.out.println("Whiteboard User Listener Running");
        try {
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());

            SetPlayerID pid = new SetPlayerID();
            pid.setPlayerID(this.id);
            Message m = Message.createMessage(this.id,pid,srv.getMapper());
            m.setSource(srv.getId());
            out.writeObject(m);

            while(true && s.isConnected()) {
                Object o = in.readObject();
                if( o instanceof Message) {
                    srv.sendMessage(this,(Message) o );
                } else {
                    System.err.println("Object was: "+o.getClass().getName());
                }
            }
            srv.removeUser(this,new Exception("User Quit"));
        } catch (IOException | ClassNotFoundException e) {
            srv.removeUser(this,e);
        }
    }
}
