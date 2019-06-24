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
    private final byte[] lookup42={(byte)  174, (byte)  0, (byte)  0, (byte)  0, (byte)  120, (byte)  94, (byte)  219, (byte)  113, (byte)  231, (byte)  26, (byte)  187, (byte)  22, (byte)  227, (byte)  138, (byte)  3, (byte)  154, (byte)  12, (byte)  64, (byte)  32, (byte)  16, (byte)  204, (byte)  144, (byte)  200, (byte)  144, (byte)  204, (byte)  80, (byte)  196, (byte)  144, (byte)  202, (byte)  144, (byte)  194, (byte)  160, (byte)  192, (byte)  224, (byte)  14, (byte)  228, (byte)  229, (byte)  2, (byte)  217, (byte)  197, (byte)  64, (byte)  92, (byte)  196, (byte)  80, (byte)  6, (byte)  38, (byte)  7, (byte)  22, (byte)  0, (byte)  0, (byte)  120, (byte)  214, (byte)  10, (byte)  193};
    private final byte[] lookup10={(byte)  174, (byte)  0, (byte)  0, (byte)  0, (byte)  120, (byte)  94, (byte)  219, (byte)  113, (byte)  231, (byte)  26, (byte)  59, (byte)  23, (byte)  227, (byte)  138, (byte)  3, (byte)  154, (byte)  12, (byte)  64, (byte)  32, (byte)  16, (byte)  204, (byte)  144, (byte)  200, (byte)  144, (byte)  204, (byte)  80, (byte)  196, (byte)  144, (byte)  202, (byte)  144, (byte)  194, (byte)  160, (byte)  192, (byte)  224, (byte)  14, (byte)  228, (byte)  229, (byte)  2, (byte)  217, (byte)  197, (byte)  64, (byte)  92, (byte)  196, (byte)  80, (byte)  6, (byte)  38, (byte)  7, (byte)  22, (byte)  0, (byte)  0, (byte)  99, (byte)  150, (byte)  10, (byte)  161};
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
        
            
            System.out.println("Listening for data");
            datagramSocket.receive(packet);
            packet.setLength(lookup10.length);
            DatagramPacket newpacket = new DatagramPacket(lookup10, lookup10.length);
            System.out.println("Forwarding this data"+packet.getData());
            datagramSocket.broadcast(newpacket, 2005);

        }
    }
}
