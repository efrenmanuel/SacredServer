/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Remote;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
 * Thread that registers connecting servers and clients
 *
 * @author efren
 */
public class ConnectionRegister implements Runnable {

    private TreeMap<String, ServerInfo> serverConnections;
    private ServerSocket serverSocket;
    private boolean running;
    private ThreadPoolExecutor serverUpdaterPool;

    /**
     * This class updates the servers that are registered in the lobbya swell as
     * the clients
     *
     * @param serverConnections Collection of servers connected to the lobby
     * @param serverSocket Socket that the lobby is listening to
     * @param serverUpdaterPool Pool that runs all the subthreads
     */
    public ConnectionRegister(boolean running, TreeMap<String, ServerInfo> serverConnections, ServerSocket serverSocket, ThreadPoolExecutor serverUpdaterPool) {
        this.serverSocket = serverSocket;
        this.serverConnections = serverConnections;
        this.serverUpdaterPool = serverUpdaterPool;
        this.running = running;
    }

    @Override
    public void run() {
        System.out.println("Started Lobby");
        while (running) {
            try {
                Socket connection = serverSocket.accept(); //Here we accept the server/client connection
                //System.out.println("Connected to the LocalServer");
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream())); //Here we create a reader to be able to know the infoa bout the server

                String connectionType = input.readLine(); //Here the connection specifies if it's a server or a client
                //System.out.println(connectionType);
                if ("server".equals(connectionType)) { //If the connection is from a server, store it's info
                    if (!serverConnections.containsKey(connection.getRemoteSocketAddress().toString())) {
                        String[] serverData = input.readLine().split("INFOSEPARATOR2019"); //Separator between data; have to change to raw bytes
                        //System.out.println("received " + serverData);
                        ServerInfo serverInfo = new ServerInfo(serverData[0], Integer.parseInt(serverData[1]), Integer.parseInt(serverData[2]), serverData[3], Integer.parseInt(serverData[4])); //Stores all of the server's info
                        serverConnections.put(connection.getRemoteSocketAddress().toString(), serverInfo); //We add the server to the server list
                        //System.out.println("Registered this ip:" + connection.getRemoteSocketAddress().toString());
                        //System.out.println("With this info: " + serverInfo.toString());
                        serverUpdaterPool.execute(()
                                -> {
                            try {
                                int tries = 10;
                                
                                while (tries > 0) {//We check if the server is still online trying to read data every second
                                    if (connection.getInputStream().read() != -1) { //If the read doesn't return -1, it means taht it's online
                                        tries = 10;
                                        String line = input.readLine();
                                        if (line != null) { //If the server sent updated info, update it
                                            String[] newServerData = line.split("INFOSEPARATOR2019");
                                            ServerInfo newServerInfo = new ServerInfo(newServerData[0], Integer.parseInt(newServerData[1]), Integer.parseInt(serverData[2]), serverData[3], Integer.parseInt(serverData[4]));
                                            serverConnections.replace(connection.getRemoteSocketAddress().toString(), newServerInfo);
                                        }
                                        Thread.sleep(1000);
                                    } else { //If the read returned -1, it means that it didn't reach the host
                                        tries--;
                                    }
                                }
                                serverConnections.remove(connection.getRemoteSocketAddress().toString()); //After trying 10 times to reach the host, delete it from the list
                            } catch (IOException ex) {
                                serverConnections.remove(connection.getRemoteSocketAddress().toString());
                            } catch (InterruptedException ex) {
                                serverConnections.remove(connection.getRemoteSocketAddress().toString());

                            }

                        }
                        );
                    } else { //An ip that was already in the list... somehow
                        //System.out.println("This ip reconnected:" + connection.getRemoteSocketAddress().toString());

                    }
                } else { //The connection is from a client. Send all of the server data
                    //System.out.println("a client connected");
                    DataOutputStream outToServer = new DataOutputStream(connection.getOutputStream());
                    for (String server : serverConnections.keySet()) {
                        outToServer.writeBytes(serverConnections.get(server).getName() + "INFOSEPARATOR2019" + serverConnections.get(server).getMaxPlayers() + "INFOSEPARATOR2019" + serverConnections.get(server).getCurrentPlayers() + "INFOSEPARATOR2019" + serverConnections.get(server).getIp() + "INFOSEPARATOR2019" + serverConnections.get(server).getPort() + "\n");
                    }
                    connection.close(); //after sending all of the server data we don't need the connection anymore, trash it.
                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionRegister.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
