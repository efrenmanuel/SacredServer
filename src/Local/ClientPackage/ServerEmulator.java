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
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author efren
 */
public class ServerEmulator implements Runnable {

    private ServerSocket serverSocket;
    private Socket socketToServer;
    private ConcurrentHashMap<String, Socket> clients;
    private ConcurrentHashMap<String, Socket> clientsForOutside;
    private ThreadPoolExecutor serverUpdaterPool;
    private boolean running;
    private int serverPort;

    public ServerEmulator(boolean running, Socket socket, InetAddress address, int serverPort) {
        try {

            System.out.println("opening port 2006");
            this.serverSocket = new ServerSocket(2006); //the default sacred game port
            this.socketToServer = socket;
            this.clients = new ConcurrentHashMap<>();
            this.clientsForOutside = new ConcurrentHashMap<>();
            this.serverUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            this.running = running;
            this.serverPort = serverPort;
        } catch (IOException ex) {
            Logger.getLogger(ServerEmulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Thread localClientUpdater = new Thread(new LocalClientUpdater(running, clients, serverSocket));
        localClientUpdater.start();
        System.out.println(socketToServer.getInetAddress().getHostAddress());
        Thread remoteClientUpdater = new Thread(new RemoteClientUpdater(running, clients, clientsForOutside, socketToServer.getInetAddress().getHostAddress(), serverPort));
        remoteClientUpdater.start();
    }

}
