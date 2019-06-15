/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local;

import Remote.ServerInfo;
import Resources.DAODatagramSocket;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
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
public class Client implements Runnable {

    private DAODatagramSocket datagramSocket;
    private Map<String, ServerInfo> serverList;
    private String localAddress;
    private String lobbyAddress;
    private Socket socket;
    private int port;

    /**
     * Client that receives the pings and broadcasts them in the local net so
     * the game can see the server aswell as taking the game data and sending it
     * to the real game
     *
     * @param lobbyAddress Address of the lobby server
     * @param port Port of the local Sacred server, Usually 2005
     */
    public Client(String lobbyAddress, int port) {
        try {
            DatagramSocket socketForProbing = new DatagramSocket();
            socketForProbing.connect(InetAddress.getByName("8.8.8.8"), 10002);
            this.lobbyAddress = lobbyAddress;
            localAddress = socketForProbing.getLocalAddress().getHostAddress();//get this pc's local address
            datagramSocket = new DAODatagramSocket(new DatagramSocket(port, InetAddress.getByName(localAddress))); //socket in this pc's local address so it can get all the game data
            socket = new Socket(lobbyAddress, 2004);
            serverList = new TreeMap<>();
        } catch (SocketException | UnknownHostException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        Thread serverListUpdater = new Thread(new ServerListUpdater(socket, serverList));
        serverListUpdater.start();
        Thread pingReceiver = new Thread(new PingReceiver(datagramSocket));
        pingReceiver.start();
        soutServer();

    }

    public void soutServer() {
        for (String server : serverList.keySet()) {
            System.out.println("ip: " + server);
            System.out.println("info: " + serverList.get(server).toString());
        }
    }

}
