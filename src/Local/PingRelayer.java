/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local;

import Resources.DAODatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class PingRelayer implements Runnable {

    private final DAODatagramSocket datagramSocket;
    private final ArrayList<String> clientAddresses;
    private final int port;

    public PingRelayer(DAODatagramSocket datagramSocket, ArrayList<String> clientAddresses, int port) {
        this.datagramSocket = datagramSocket;
        this.clientAddresses = clientAddresses;
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
                packet.setPort(port);
                System.out.println(packet.getData().toString());
                for (String address : clientAddresses) {
                    packet.setAddress(InetAddress.getByName(address));
                    System.out.println("Sending ping to " + address);
                    datagramSocket.send(packet);
                    System.out.println("Data sent.");
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(PingRelayer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
