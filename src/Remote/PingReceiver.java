/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Remote;

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
    private Map<String, Server> servers;

    public PingReceiver(DAODatagramSocket datagramSocket, Map<String, Server> servers) {
        this.datagramSocket = datagramSocket;
        this.servers = servers;
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
            if (servers.containsKey(packet.getAddress().toString())) {
                packet.setLength(packet.getLength());
                for (InetAddress address : servers.get(packet.getAddress().toString()).getClients()) {
                    datagramSocket.send(packet.getData(), address.getHostAddress(), 2007);
                }
            }else{
                servers.put(packet.getAddress().toString(), new Server(packet.getSocketAddress()));
            }

        }
    }
}
