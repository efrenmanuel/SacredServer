/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private TreeMap<String, ServerInfo> serverConnections;
    private ServerSocket serverSocket;
    private boolean updating;
    private ThreadPoolExecutor serverUpdaterPool;

    public ServerRegister(TreeMap<String, ServerInfo> serverConnections, ServerSocket serverSocket, ThreadPoolExecutor serverUpdaterPool) {
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
                System.out.println("Connected to the LocalServer");
                if (!serverConnections.containsKey(connection.getRemoteSocketAddress().toString())) {
                    BufferedReader input = new BufferedReader (new InputStreamReader (connection.getInputStream()));
                    String[] serverData = input.readLine().split("INFOSEPARATOR2019");
                    System.out.println("received "+serverData);
                    ServerInfo serverInfo=new ServerInfo(serverData[0], Integer.parseInt(serverData[1]), Integer.parseInt(serverData[2]));
                    serverConnections.put(connection.getRemoteSocketAddress().toString(), serverInfo);
                    System.out.println("Registered this ip:" + connection.getRemoteSocketAddress().toString());
                    System.out.println("With this info: "+serverInfo.toString());
                    serverUpdaterPool.execute(() ->
                    {
                        try {
                            int tries=10;
                            while (connection.getInputStream().read()!=-1 && tries>0){
                                tries=10;
                                String line = input.readLine();
                                if (line != null){
                                    String[] newServerData= line.split("INFOSEPARATOR2019");
                                    ServerInfo newServerInfo=new ServerInfo(newServerData[0], Integer.parseInt(newServerData[1]), Integer.parseInt(newServerData[2]));
                                    serverConnections.replace(connection.getRemoteSocketAddress().toString(), newServerInfo);
                                }
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
