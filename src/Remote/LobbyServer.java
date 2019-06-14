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
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class LobbyServer implements Runnable{

    private ArrayList<InetAddress> clients;
    private TreeMap<String, ServerInfo> serverConnections;
    private ServerSocket serverSocket;
    private ThreadPoolExecutor serverUpdaterPool;

    public LobbyServer(int serverPort, int clientPort){
        try {
            this.serverConnections = new TreeMap<>();
            this.clients = new ArrayList<>();
            serverSocket = new ServerSocket(serverPort);
            serverUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        } catch (IOException ex) {
            Logger.getLogger(LobbyServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<InetAddress> getClients() {
        return clients;
    }

    @Override
    public void run() {
        Thread serverUpdater = new Thread(new ServerRegister(serverConnections, serverSocket, serverUpdaterPool));
        serverUpdater.start();
        
       /* while (true){
            if (serverConnections.size()==0){
                System.out.println("No connections");
            }else{
                System.out.println(serverConnections.size()+" connections");
                for (String address:serverConnections.keySet()){
                    System.out.println(address);
                }
            }
        }*/
        
    }

}
