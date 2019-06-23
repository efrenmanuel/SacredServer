/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.ClientPackage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class LocalClientUpdater implements Runnable {

    private Map<InetAddress, Socket> clients;
    private ServerSocket serverSocket;
    private boolean updating;

    private ThreadPoolExecutor clientUpdaterPool;

    public LocalClientUpdater(Map<InetAddress, Socket> clients, ServerSocket serverSocket) {
        this.clients = clients;
        this.serverSocket = serverSocket;
        updating = true;
        this.clientUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    }

    @Override
    public void run() {
        while (updating) {
            try {
                System.out.println("Listening in port 2006");
                System.out.println("listening in:" + serverSocket.getInetAddress().getHostAddress());
                Socket connection = serverSocket.accept();
                System.out.println("Client connected");
                clients.put(connection.getInetAddress(), connection);

                clientUpdaterPool.execute(()
                        -> {
                    try {
                        int tries = 10;
                        while (connection.getInputStream().read() != -1 && tries > 0) {
                            tries = 10;
                            Thread.sleep(1500);
                        }
                    } catch (IOException ex) {
                        clients.remove(connection.getInetAddress());
                    } catch (InterruptedException ex) {
                        clients.remove(connection.getInetAddress());
                    }

                }
                );

            } catch (IOException ex) {
                Logger.getLogger(LocalClientUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
