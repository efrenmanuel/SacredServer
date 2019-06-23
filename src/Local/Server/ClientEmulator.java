/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.Server;

import Remote.ServerInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class ClientEmulator implements Runnable {

    private TreeMap<String, Socket> clientConnections;

    private TreeMap<String, Socket> fakeClients;
    private int startIp;
    private int gamePort;
    private boolean updating;
    
    private ThreadPoolExecutor clientUpdaterPool;

    public ClientEmulator(ServerSocket serverSocket, TreeMap<String, Socket> clientConnections,TreeMap<String, Socket> fakeClients, int gamePort, int startIp) {
        this.startIp = startIp;
        this.clientConnections = clientConnections;
        this.gamePort = gamePort;
        this.fakeClients=fakeClients;
        this.clientUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        updating=true;

    }

    @Override
    public void run() {
        while (true) {
            for (String client : clientConnections.keySet()) {
                if (!fakeClients.containsKey(client)) {
                    try {
                        fakeClients.put(client, new Socket(InetAddress.getLocalHost(), gamePort, InetAddress.getLocalHost(), 2006+startIp));
                        System.out.println("Created this client: "+fakeClients.get(client).getLocalSocketAddress());
                        startIp++;
                        clientUpdaterPool.execute(()
                                -> {
                            try {
                                int tries = 10;

                                while (updating) {
                                    tries = 10;
                                    Socket listenTo = fakeClients.get(client);
                                    InputStream input = listenTo.getInputStream();
                                    
                                    Socket sendTo = clientConnections.get(client);
                                    OutputStream output = sendTo.getOutputStream();
                                    byte[] buffer = new byte[input.available()];
                                    input.read(buffer);
                                    output.write(buffer);

                                    Thread.sleep(100);
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ClientEmulator.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(ClientEmulator.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        }
                        );
                        clientUpdaterPool.execute(()
                                -> {
                            try {
                                int tries = 10;

                                while (updating) {
                                    tries = 10;
                                    Socket listenTo = clientConnections.get(client);
                                    InputStream input = listenTo.getInputStream();

                                    Socket sendTo = fakeClients.get(client);
                                    OutputStream output = sendTo.getOutputStream();
                                    byte[] buffer = new byte[input.available()];
                                    input.read(buffer);
                                    output.write(buffer);

                                    Thread.sleep(100);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ClientEmulator.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ClientEmulator.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        );
                        
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(ClientEmulator.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ClientEmulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }

}
