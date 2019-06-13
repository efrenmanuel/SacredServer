/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local;

import Resources.DAODatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class LocalServer implements Runnable {

    DAODatagramSocket datagramSocket;
    Socket socket;
    ArrayList<String> clientAddresses;
    String lobbyAddress;
    int port;

    /**
     * Local Server that takes everything from the game server and distributes it to the clients and emulates the clients in the network with ips from 192.168.1.150 to 192.168.1.166 (max players is 66)
     * @param clientAddress Address of the lobby server
     * @param port Port of the local Sacred server, Usually 2005
     */
    public LocalServer(String lobbyAddress, int port) {
        try {

            datagramSocket = new DAODatagramSocket(new DatagramSocket(port));
            this.clientAddresses=new ArrayList<>();
            this.port=port;
            this.lobbyAddress=lobbyAddress;
        } catch (SocketException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void run() {
        Thread pingRelayer = new Thread(new PingRelayer(datagramSocket, clientAddresses, 2004)); //2004 port for the remote server
        pingRelayer.start();

    }

}
