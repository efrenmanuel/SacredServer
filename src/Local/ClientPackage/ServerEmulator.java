/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.ClientPackage;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 *
 * @author efren
 */
public class ServerEmulator implements Runnable {

    private ServerSocket serverSocket;
    private Socket socketToServer;
    private Map<String, Socket> clients;
    private Map<String, Socket> clientsForOutside;
    private ThreadPoolExecutor serverUpdaterPool;

    public ServerEmulator(Socket socket, InetAddress address){
        try {
            
            System.out.println("opening port 2006");
            this.serverSocket = new ServerSocket(2006);
            this.socketToServer = socket;
            this.clients = new TreeMap<>();
            this.clientsForOutside = new TreeMap<>();
            this.serverUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        } catch (IOException ex) {
            Logger.getLogger(ServerEmulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Thread localClientUpdater = new Thread(new LocalClientUpdater(clients, serverSocket));
        localClientUpdater.start();
        System.out.println(socketToServer.getInetAddress().getHostAddress());
        Thread remoteClientUpdater = new Thread(new RemoteClientUpdater(clients, clientsForOutside, socketToServer.getInetAddress().getHostAddress(), 2003));
        remoteClientUpdater.start();
    }
    
    

}
