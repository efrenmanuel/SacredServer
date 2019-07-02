/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.ClientPackage;

import Resources.Forwarder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class RemoteClientUpdater implements Runnable {

    private ConcurrentHashMap<String, Socket> clients;
    private ConcurrentHashMap<String, Socket> clientsForOutside;
    private boolean running;
    private String serverIP;
    private int serverPort;
    private int startPort;
    private ThreadPoolExecutor clientUpdaterPool;
    private static final int READ_BUFFER_SIZE = 8192;

    public RemoteClientUpdater(boolean running, ConcurrentHashMap<String, Socket> clients, ConcurrentHashMap<String, Socket> clientsForOutside, String serverIP, int serverPort) {
        this.clients = clients;
        this.clientsForOutside = clientsForOutside;
        this.serverPort = serverPort;
        this.running = running;
        this.serverIP = serverIP;
        this.clientUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        this.startPort = 2008;

    }

    @Override
    public void run() {
        while (running) {
            for (String address : clients.keySet()) {
                System.out.println("Processing this client: " + address + clients.get(address).getLocalPort());
                if (!clientsForOutside.containsKey(address)) {
                    try {
                        Socket connection = new Socket(InetAddress.getByName(serverIP), serverPort);
                        clientsForOutside.put(address, connection);
                        clientUpdaterPool.execute(new Forwarder(clients, clientsForOutside, address));
                        clientUpdaterPool.execute(new Forwarder(clientsForOutside, clients, address));

                    } catch (IOException ex) {
                        Logger.getLogger(RemoteClientUpdater.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(RemoteClientUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
