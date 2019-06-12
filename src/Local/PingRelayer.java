/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local;

import Remote.Client;
import Resources.DAODatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class PingRelayer implements Runnable {

    private final DAODatagramSocket datagramSocket;
    private final String lobbyAddress;
    private final int port;

    public PingRelayer(DAODatagramSocket datagramSocket, String lobbyAddress, int port) {
        this.datagramSocket = datagramSocket;
        this.lobbyAddress = lobbyAddress;
        this.port = port;
    }

   
    @Override
    public void run() {
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {
            try {
                packet.setLength(512);
                System.out.println("Listening");
                datagramSocket.receive(packet);
                packet.setLength(packet.getLength());
                packet.setAddress(InetAddress.getByName(lobbyAddress));
                packet.setPort(port);
                System.out.println("Sending data.");
                datagramSocket.send(packet);
                System.out.println("Data sent.");
            } catch (UnknownHostException ex) {
                Logger.getLogger(PingRelayer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
