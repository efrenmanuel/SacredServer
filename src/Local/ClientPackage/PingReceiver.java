/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.ClientPackage;

import Remote.LobbyServer;
import Resources.DAODatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Map;

/**
 *
 * @author efren
 */
public class PingReceiver implements Runnable {

    private final DAODatagramSocket datagramSocket;
    
    /**
     * Receives the pings from the main server sent to port 2004 and broadcasts them in the local net
     * @param datagramSocket local pc socket
     */
    public PingReceiver(DAODatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {
        
            packet.setLength(512);
            System.out.println("Listening for data");
            datagramSocket.receive(packet);
            System.out.println("Forwarding this data"+packet.getData());
            datagramSocket.broadcast(packet, 2005);

        }
    }
}
