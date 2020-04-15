package com.derp.hurr.whiteboard;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class WhiteboardUser implements Runnable {

    Socket s;
    ObjectInputStream in;
    ObjectOutputStream out;
    WhiteboardServer srv;
    public final UUID id = UUID.randomUUID();

    public WhiteboardUser(Socket s,WhiteboardServer srv) {
        this.srv = srv;
        this.s = s;

    }

    public void giveMessage(ArrayList<Point> pnts) {
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

            while(true && s.isConnected()) {
                Object o = in.read();
                if( o instanceof ArrayList) {
                    srv.sendMessage(this,(ArrayList<Point>) o );
                } else {
                    System.err.println("Object was: "+o.getClass().getName());
                }
            }
            srv.removeUser(this,new Exception("User Quit"));
        } catch (IOException e) {
            srv.removeUser(this,e);
        }
    }
}
