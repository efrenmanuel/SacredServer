/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.ClientPackage;

import Remote.ServerInfo;
import Resources.DAODatagramSocket;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class Client{

    private DAODatagramSocket datagramSocket;
    private Map<String, ServerInfo> serverList;
    private String localAddress;
    private Socket socketLobby;
    private Socket socketServer;
    private boolean running;

    /**
     * Client that receives the pings and broadcasts them in the local net so
     * the game can see the server aswell as taking the game data and sending it
     * to the real game
     *
     * @param lobbyAddress Address of the lobby server
     * @param port Port of the local Sacred server, Usually 2005
     */
    public Client(String lobbyAddress, int port, int portUDP) {
        try {
            DatagramSocket socketForProbing = new DatagramSocket();
            socketForProbing.connect(InetAddress.getByName("8.8.8.8"), 10002);

            localAddress = socketForProbing.getLocalAddress().getHostAddress();//get this pc's local address
            datagramSocket = new DAODatagramSocket(new DatagramSocket(portUDP, InetAddress.getByName(localAddress))); //socket in this pc's local address so it can get all the game data

            socketLobby = new Socket(lobbyAddress, port); //Socket to be able to connect tot he lobby and get the server info

            serverList = new TreeMap<>();
            
            updateServers();
        } catch (SocketException | UnknownHostException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public final void updateServers() {
        Thread serverListUpdater = new Thread(new ServerListUpdater(socketLobby, serverList));
        serverListUpdater.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Connect to a remote server
     * @param address Address of the server that we are connecting to
     * @param socket socket that the server is listening to
     * @return connected socket
     */
    public Socket Connect(String address) {
        try {
            //System.out.println("Connecting to " + address);
            running=true;
            Socket socket;
            socket = new Socket(serverList.get(address).getIp(), serverList.get(address).getPort());
            System.out.println("Connected");
            Thread serverEmulator = new Thread(new ServerEmulator(running, socketServer, datagramSocket.getInetAddress(), serverList.get(address).getPort()));
            serverEmulator.start();
            Thread pingReceiver = new Thread(new PingReceiver(running, datagramSocket));
            pingReceiver.start();
            return socket;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
