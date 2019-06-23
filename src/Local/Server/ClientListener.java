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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class ClientListener implements Runnable {

    private TreeMap<String, Socket> clientConnections;
    ServerSocket serverSocket;
    boolean updating;

    private ThreadPoolExecutor clientUpdaterPool;

    public ClientListener(ServerSocket serversocket, TreeMap<String, Socket> clientConnections) {
        this.serverSocket = serversocket;
        this.clientConnections=clientConnections;
        this.updating = true;
        this.clientUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        while (updating) {
            try {
                Socket connection = serverSocket.accept();
                System.out.println("Connected to the LocalServer");
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                if (!clientConnections.containsKey(connection.getRemoteSocketAddress().toString())) {
                    clientConnections.put(connection.getRemoteSocketAddress().toString(), connection);
                    System.out.println("Registered this ip:" + connection.getRemoteSocketAddress().toString());
                    clientUpdaterPool.execute(()
                            -> {
                        try {
                            int tries = 10;
                            while (connection.getInputStream().read() != -1 && tries > 0) {
                                tries = 10;
                                String line = input.readLine();
                                if (line != null) {
                                    clientConnections.replace(connection.getRemoteSocketAddress().toString(), connection);
                                }
                                Thread.sleep(1500);
                            }
                        } catch (IOException ex) {
                            clientConnections.remove(connection.getRemoteSocketAddress().toString());
                        } catch (InterruptedException ex) {
                            clientConnections.remove(connection.getRemoteSocketAddress().toString());
                        }

                    }
                    );
                } else {
                    System.out.println("This ip reconnected:" + connection.getRemoteSocketAddress().toString());

                }

            } catch (IOException ex) {
                System.out.println("Error" + ex);
            }
        }

    }

}
