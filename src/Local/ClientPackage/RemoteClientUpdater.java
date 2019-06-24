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

    private Map<String, Socket> clients;
    private Map<String, Socket> clientsForOutside;
    private boolean updating;
    private String serverIP;
    private int serverPort;
    private int startPort;
    private ThreadPoolExecutor clientUpdaterPool;

    public RemoteClientUpdater(Map<String, Socket> clients, Map<String, Socket> clientsForOutside, String serverIP, int serverPort) {
        this.clients = clients;
        this.clientsForOutside = clientsForOutside;
        this.serverPort = serverPort;
        updating = true;
        this.serverIP = serverIP;
        this.clientUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        this.startPort = 2008;

    }

    @Override
    public void run() {
        while (updating) {
            for (String address : clients.keySet()) {
                System.out.println("Processing this client: " + address);
                if (!clientsForOutside.containsKey(address)) {
                    try {
                        Socket connection = new Socket(InetAddress.getByName(serverIP), serverPort);
                        clientsForOutside.put(address, connection);
                        clientUpdaterPool.execute(()
                                -> {
                            int tries = 10;
                            try {
                                while (tries > 0) {
                                    if (clients.get(address).getInputStream().read() == -1) {
                                        tries -= 1;
                                    } else {
                                        tries = 10;
                                    }
                                    Thread.sleep(150);
                                }
                                clients.remove(address);
                            } catch (InterruptedException | IOException ex) {
                                clients.remove(address);
                            }
                        }
                        );
                        clientUpdaterPool.execute(()
                                -> {
                            try {

                                while (updating) {
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
                                while (updating) {
                                    Socket listenTo = clients.get(address);
                                    InputStream input = listenTo.getInputStream();

                                    Socket sendTo = clientsForOutside.get(address);
                                    OutputStream output = sendTo.getOutputStream();
                                    byte[] buffer = new byte[input.available()];
                                    input.read(buffer);
                                    output.write(buffer);

                                    Thread.sleep(100);
                                }
                            } catch (IOException | InterruptedException ex) {
                                System.out.println("ERROR: "+ex);
                            }
                        }
                        );
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
