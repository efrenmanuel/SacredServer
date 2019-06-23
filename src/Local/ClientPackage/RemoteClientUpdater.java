/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.ClientPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class RemoteClientUpdater implements Runnable {

    private Map<InetAddress, Socket> clients;
    private Map<InetAddress, Socket> clientsForOutside;
    private boolean updating;
    private String serverIP;
    private int serverPort;
    private ThreadPoolExecutor clientUpdaterPool;

    public RemoteClientUpdater(Map<InetAddress, Socket> clients, Map<InetAddress, Socket> clientsForOutside, String serverIP, int serverPort) {
        this.clients = clients;
        this.clientsForOutside = clientsForOutside;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        updating = true;
        this.clientUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    }

    @Override
    public void run() {
        while (updating) {
            for (InetAddress address : clients.keySet()) {
                if (!clientsForOutside.containsKey(address)) {
                    try {
                        Socket connection = new Socket(InetAddress.getByName(serverIP), serverPort);
                        clientsForOutside.put(address, connection);
                        clientUpdaterPool.execute(()
                                -> {
                            try {
                                int tries = 10;

                                while (updating) {
                                    tries = 10;
                                    Socket listenTo = clientsForOutside.get(address);
                                    InputStream input = listenTo.getInputStream();
                                    
                                    Socket sendTo = clients.get(address);
                                    OutputStream output = sendTo.getOutputStream();
                                    byte[] buffer = new byte[input.available()];
                                    input.read(buffer);
                                    output.write(buffer);

                                    Thread.sleep(100);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(RemoteClientUpdater.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(RemoteClientUpdater.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        );
                        clientUpdaterPool.execute(()
                                -> {
                            try {
                                int tries = 10;

                                while (updating) {
                                    tries = 10;
                                    Socket listenTo = clients.get(address);
                                    InputStream input = listenTo.getInputStream();

                                    Socket sendTo = clientsForOutside.get(address);
                                    OutputStream output = sendTo.getOutputStream();
                                    byte[] buffer = new byte[input.available()];
                                    input.read(buffer);
                                    output.write(buffer);

                                    Thread.sleep(100);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(RemoteClientUpdater.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(RemoteClientUpdater.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        );
                    } catch (IOException ex) {
                        Logger.getLogger(RemoteClientUpdater.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }

    }
}
