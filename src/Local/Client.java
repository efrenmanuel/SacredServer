/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local;

import Resources.DAODatagramSocket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author efren
 */
public class Client implements Runnable {
    private DAODatagramSocket datagramSocket;
    private String localAddress;
    private String lobbyAddress;
    private int port;
    
    /**
     * Client that receives the pings and broadcasts them in the local net so the game can see the server aswell as taking the game data and sending it to the real game
     * @param lobbyAddress Address of the lobby server
     * @param port Port of the local Sacred server, Usually 2005
     */
    public Client(String lobbyAddress, int port) {
        try {
            DatagramSocket socketForProbing = new DatagramSocket();
            socketForProbing.connect(InetAddress.getByName("8.8.8.8"), 10002);
            this.lobbyAddress=lobbyAddress;
            localAddress=socketForProbing.getLocalAddress().getHostAddress();//get this pc's local address
            datagramSocket = new DAODatagramSocket(new DatagramSocket(port,InetAddress.getByName(localAddress))); //socket in this pc's local address so it can get all the game data
            
        } catch (SocketException | UnknownHostException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void run() {
        Thread pingReceiver= new Thread(new PingReceiver(datagramSocket));
        pingReceiver.start();

    }

}
