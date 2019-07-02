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

    private Map<String, Socket> clients;
    private ServerSocket serverSocket;
    private boolean running;

    private ThreadPoolExecutor clientUpdaterPool;

    public LocalClientUpdater(boolean running, Map<String, Socket> clients, ServerSocket serverSocket) {
        this.clients = clients;
        this.serverSocket = serverSocket;
        this.running = running;
        this.clientUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    }

    @Override
    public void run() {
        while (running) {
            try {
                System.out.println("Listening in port 2006");
                System.out.println("listening in:" + serverSocket.getInetAddress().getHostAddress());
                Socket connection = serverSocket.accept();
                System.out.println("Game connected");
                clients.put(connection.getInetAddress().getHostAddress(), connection);

            } catch (IOException ex) {
                Logger.getLogger(LocalClientUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
