/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Remote;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class ServerRegister implements Runnable {

    private TreeMap<String, Socket> serverConnections;
    private ServerSocket serverSocket;
    private boolean updating;
    private ThreadPoolExecutor serverUpdaterPool;

    public ServerRegister(TreeMap<String, Socket> serverConnections, ServerSocket serverSocket, ThreadPoolExecutor serverUpdaterPool) {
        this.serverSocket = serverSocket;
        this.serverConnections = serverConnections;
        this.serverUpdaterPool=serverUpdaterPool;
        updating = true;
    }

    @Override
    public void run() {
        while (updating) {
            try {
                Socket connection = serverSocket.accept();
                if (!serverConnections.containsKey(connection.getRemoteSocketAddress().toString())) {
                    serverConnections.put(connection.getRemoteSocketAddress().toString(), connection);
                    System.out.println("Registered this ip:" + connection.getRemoteSocketAddress().toString());
                    
                    serverUpdaterPool.execute(() ->
                    {
                        try {
                            int tries=10;
                            while (connection.getInputStream().read()!=-1 && tries>0){
                                tries=10;
                                Thread.sleep(1500);
                            };
                        } catch (IOException ex) {
                            serverConnections.remove(connection.getRemoteSocketAddress().toString());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ServerRegister.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                    ); 
                }else{
                    System.out.println("This ip reconnected:" + connection.getRemoteSocketAddress().toString());
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerRegister.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
