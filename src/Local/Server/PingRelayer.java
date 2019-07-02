/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.Server;

import Resources.DAODatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class takes the broadcasted ping from port 2005 that makes the game know there is a server and forwards it to the clients
 * @author efren
 */
public class PingRelayer implements Runnable {

    private final DAODatagramSocket datagramSocket; 
    private final ConcurrentHashMap<String, Socket> clientConnections; 
    private final int port; 
    private boolean running;
    
    /**
     * 
     * @param datagramSocket //Socket that will receive the broadcast and seend it to the client
     * @param clientConnections //List of clients to send the broadcast to
     * @param port //port that the client has open and is listening to (must be different from 2005)
     */
    public PingRelayer(boolean running, DAODatagramSocket datagramSocket, ConcurrentHashMap<String, Socket> clientConnections, int port) {
        this.datagramSocket = datagramSocket;
        this.clientConnections = clientConnections;
        this.port = port;
        this.running=running;
    }

    @Override
    public void run() {
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (running) {
            packet.setLength(512);
            //System.out.println("Listening");
            datagramSocket.receive(packet); // Intercepting the local broadcast
            packet.setLength(packet.getLength());
            packet.setPort(port);
            //System.out.println(packet.getData().toString());
            for (String address : clientConnections.keySet()) { // Sending it to every registered client
                packet.setAddress(clientConnections.get(address).getInetAddress());
                System.out.println("Sending ping to " + address);
                datagramSocket.send(packet);
                System.out.println("Data sent.");
            }
        }

    }
}

