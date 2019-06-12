/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Remote;

import Local.PingRelayer;
import Resources.DAODatagramSocket;
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
public class Client implements Runnable {
    private DAODatagramSocket datagramSocket;
    private Socket socket;
    private String localAddress;
    private String lobbyAddress;
    private int port;
    private Map<String, Server> servers;
    
    /**
     * 
     * @param lobbyAddress Address of the lobby server
     * @param port Port of the local Sacred server, Usually 2005
     */
    public Client(String lobbyAddress, int port) {
        try {
            DatagramSocket socketForProbing = new DatagramSocket();
            socketForProbing.connect(InetAddress.getByName("8.8.8.8"), 10002);
            localAddress=socketForProbing.getLocalAddress().getHostAddress();
            this.lobbyAddress=lobbyAddress;
            datagramSocket = new DAODatagramSocket(new DatagramSocket(port,InetAddress.getByName(localAddress)));
            servers = new TreeMap<>();
            this.localAddress=localAddress;
        } catch (SocketException ex) {
            System.out.println(ex);
        } catch (UnknownHostException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void run() {
        Thread pingReceiver= new Thread(new PingReceiver(datagramSocket,servers));
        pingReceiver.start();

    }

}
