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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class PingRelayer implements Runnable {

    private final DAODatagramSocket datagramSocket;
    private final TreeMap<String, Socket> clientConnections;
    private final int port;

    public PingRelayer(DAODatagramSocket datagramSocket, TreeMap<String, Socket> clientConnections, int port) {
        this.datagramSocket = datagramSocket;
        this.clientConnections = clientConnections;
        this.port = port;
    }

    @Override
    public void run() {
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {
            packet.setLength(512);
            System.out.println("Listening");
            datagramSocket.receive(packet);
            packet.setLength(packet.getLength());
            packet.setPort(2008);
            System.out.println(packet.getData().toString());
            for (String address : clientConnections.keySet()) {
                packet.setAddress(clientConnections.get(address).getInetAddress());
                System.out.println("Sending ping to " + address);
                datagramSocket.send(packet);
                System.out.println("Data sent.");
            }
        }

    }
}

