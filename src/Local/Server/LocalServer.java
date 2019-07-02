/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.Server;

import Remote.ServerInfo;
import Resources.DAODatagramSocket;
import Resources.DAOSocket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class LocalServer {

    DAODatagramSocket datagramSocket;
    ServerSocket serverSocket;
    Socket socket;
    DAOSocket dSocket;
    ArrayList<String> clientAddresses;
    String lobbyAddress;
    int port;
    private ConcurrentHashMap<String, Socket> clientConnections;
    private ConcurrentHashMap<String, Socket> fakeClients;
    Thread pingRelayer, clientListener, clientEmulator;
    boolean running;

    /**
     * Local Server that takes everything from the game server and distributes
     * it to the clients as well as emulating the clients in the network with
     * ports from 2008 onwards
     *
     * @param lobbyAddress Address of the lobby to register as online
     * @param serverName Name that the server will have
     * @param maxPlayers Number of maximum players
     * @param port Port That this server will listen to
     * @throws Local.Server.LocalServer.LobbyServerNotAvailable The lobby isn't
     * available
     */
    public LocalServer(String lobbyAddress, String serverName, int maxPlayers, int port) throws LobbyServerNotAvailable {
        clientConnections = new ConcurrentHashMap<>();
        this.fakeClients = new ConcurrentHashMap<>();
        try {
            String systemipaddress;
  
            serverSocket = new ServerSocket(port); // Opening the socket to let the clients connect

            datagramSocket = new DAODatagramSocket(new DatagramSocket(2005)); // UDP socket that the game sends ping to let itself be known in the network, always 2005
            this.clientAddresses = new ArrayList<>(); // List of connected clients
            this.port = port;
            this.lobbyAddress = lobbyAddress;
            try { //We find the public address, in case the lobby and server share network
                URL url_name = new URL("http://bot.whatismyipaddress.com");

                BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));

                // reads system IPAddress
                systemipaddress = sc.readLine().trim();
            } catch (IOException e) {
                systemipaddress = "Cannot Execute Properly";
            }
            //System.out.println("Public IP Address: " + systemipaddress + "\n");
            try {
                socket = new Socket(lobbyAddress, 2004); // We open a client socket to connect tot the lobby
                DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); // Output stream to send strings to the lobby
                outToServer.writeBytes("server\n"); // Identifying as server
                //System.out.println("sent server");
                outToServer.writeBytes(serverName + "INFOSEPARATOR2019" + maxPlayers + "INFOSEPARATOR2019" + 0 + "INFOSEPARATOR2019" + systemipaddress + "INFOSEPARATOR2019" + port + "\n"); // Sending the server info
                //System.out.println("sent " + serverName + "INFOSEPARATOR2019" + maxPlayers + "INFOSEPARATOR2019" + 0 + "INFOSEPARATOR2019" + systemipaddress + "INFOSEPARATOR2019" + port + "\n");
            } catch (IOException ex) {
                throw new LobbyServerNotAvailable(ex); //Oops! that lobby server must be offline!
            }
        } catch (SocketException ex) {
            Logger.getLogger(LocalServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LocalServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        running=true;
        Thread pingRelayer = new Thread(new PingRelayer(running, datagramSocket, clientConnections, 2008));
        pingRelayer.start();
        //System.out.println("Started relayer");
        Thread clientListener = new Thread(new ClientListener(running, serverSocket, clientConnections));
        clientListener.start();
        //System.out.println("Started listener");
        Thread clientEmulator = new Thread(new ClientEmulator(running, clientConnections, fakeClients, 2006, 150));
        clientEmulator.start();
        //System.out.println("Started emulator");

    }
    
    public void stop(){
        running=false;
    }

    public class LobbyServerNotAvailable extends Exception {

        public LobbyServerNotAvailable(Throwable err) {
            super("The specified server is not available", err);
        }
    }
}
