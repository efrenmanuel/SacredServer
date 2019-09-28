/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Remote;

import Resources.DAODatagramSocket;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lobby server to keep all the server IP's in a place that the clients can connect to
 * @author efren
 */
public class LobbyServer{

    private ArrayList<InetAddress> clients;
    private TreeMap<String, ServerInfo> serverConnections;
    private ServerSocket serverSocket;
    private ThreadPoolExecutor serverUpdaterPool;
    private boolean running;

    /**
     * Main lobby server
     * @param serverPort Port that the lobby will listen to. it has to have been forwarded towards the server
     */
    public LobbyServer(boolean running, int serverPort){
        try {
            this.serverConnections = new TreeMap<>(); // Collection where we will store the servers
            this.clients = new ArrayList<>();         // Collection where we will store the clients
            serverSocket = new ServerSocket(serverPort); //Socket that the lobby will listen to
            serverUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool(); //Pool to execute the threads where we update all the info
            this.running=running;
        } catch (IOException ex) {
            Logger.getLogger(LobbyServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<InetAddress> getClients() {
        return clients;
    }

    public void run() {
        Thread serverUpdater = new Thread(new ConnectionRegister(running, serverConnections, serverSocket, serverUpdaterPool));
        serverUpdater.start();
    }
    
    public Collection<ServerInfo> getServers(){
        return serverConnections.values();
    }

}
