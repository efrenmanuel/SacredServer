/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.Server;

import Remote.ServerInfo;
import Resources.Forwarder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class emulates the client's for the game server to think that the people
 * playing the game is in the same network as it is
 *
 * @author efren
 */
public class ClientEmulator implements Runnable {

    private ConcurrentHashMap<String, Socket> clientConnections;

    private ConcurrentHashMap<String, Socket> fakeClients;
    private int startIp;
    private int gamePort;
    private boolean running;

    private ThreadPoolExecutor clientUpdaterPool;

    /**
     * This class emulates the client's for the game server to think that the
     * people playing the game is in the same network as it is
     *
     * @param clientConnections Collection that has all the real clients in to
     * seend the information back to them
     * @param fakeClients Colleciton that has all of the fake clients in
     * @param gamePort Port that the game is listening to to send all of the
     * client's comunications
     * @param startIp Port that the clients have a range from to be separated
     */
    public ClientEmulator(boolean running, ConcurrentHashMap<String, Socket> clientConnections, ConcurrentHashMap<String, Socket> fakeClients, int gamePort, int startIp) {
        this.startIp = startIp;
        this.clientConnections = clientConnections;
        this.gamePort = gamePort;
        this.fakeClients = fakeClients;
        this.clientUpdaterPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        this.running = running;

    }

    @Override
    public void run() {
        while (running) {
            for (String client : clientConnections.keySet()) { //For each connected client
                if (!fakeClients.containsKey(client)) { //If there isn't a corresponding fake client
                    try {
                        fakeClients.put(client, new Socket(InetAddress.getLocalHost(), gamePort, InetAddress.getLocalHost(), 2006 + startIp)); //Create a new fakee client with the same "name"
                        //System.out.println("Created this client: " + fakeClients.get(client).getLocalSocketAddress());
                        startIp++; //Add one tot he port for the next client

                        clientUpdaterPool.execute(new Forwarder(fakeClients, clientConnections, client)); //Execute a forwarder that will send all of the info between the fake and real client
                        clientUpdaterPool.execute(new Forwarder(clientConnections, fakeClients, client));

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
