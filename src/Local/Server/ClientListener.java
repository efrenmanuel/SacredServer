/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.Server;

import Remote.ServerInfo;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that listens for clients
 * @author efren
 */
public class ClientListener implements Runnable {

    private ConcurrentHashMap<String, Socket> clientConnections;
    ServerSocket serverSocket;
    boolean running;

    private ThreadPoolExecutor clientUpdaterPool;
    
    /**
     * 
     * @param serversocket socket that the server will listen to
     * @param clientConnections collection of all the clients
     */
    public ClientListener(boolean running, ServerSocket serversocket, ConcurrentHashMap<String, Socket> clientConnections) {
        this.serverSocket = serversocket;
        this.clientConnections = clientConnections;
        this.running = running;
        this.clientUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket connection = serverSocket.accept(); //A client connects
                //System.out.println("Connected to the LocalServer");
                if (!clientConnections.containsKey(connection.getRemoteSocketAddress().toString())) { //If this client doesn't exist already
                    clientConnections.put(connection.getRemoteSocketAddress().toString(), connection); //Create a new client with it's info
                    //System.out.println("Registered this ip:" + connection.getRemoteSocketAddress().toString());

                } else { //If the client was already in
                    //System.out.println("This ip reconnected:" + connection.getRemoteSocketAddress().toString()); ()

                }

            } catch (IOException ex) {
                System.out.println("Error" + ex);
            }
        }

    }

}
