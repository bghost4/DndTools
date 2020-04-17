package com.derp.hurr.whiteboard;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class WhiteboardServer {

    ServerSocket ss;
    private final int p;
    UUID id;
    ArrayList<WhiteboardUser> users = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();

    public WhiteboardServer(int port) {
        p = port;

        mapper.enableDefaultTyping();
        id = UUID.randomUUID();

        runServer();

    }

    public UUID getId() { return id; }

    public ObjectMapper getMapper() { return mapper; }

    public void runServer() {

            try {
                ss = new ServerSocket(p);
                while(true) {
                    Socket client = ss.accept();

                    System.out.println("Client Connected");

                    WhiteboardUser user = new WhiteboardUser(client, this);
                    users.add(user);
                    Thread t = new Thread(user);
                    t.setDaemon(true);
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    public static void main(String[] args) {
        new WhiteboardServer(8888);
    }

    public synchronized void removeUser(WhiteboardUser user,Exception e) {
        users.remove(user);

        System.out.println("User quit because: ");
        e.printStackTrace();

    }

    public synchronized void sendMessage(WhiteboardUser from,Message b) {
        System.out.println("Sending Message from: "+from.id);
        users.stream().filter( u -> u != from).forEach( u -> u.giveMessage(b));
    }


}
