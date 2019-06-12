/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class DAODatagramSocket {

    private DatagramSocket datagramSocket;
    private ServerSocket serverSocket;

    public DAODatagramSocket(DatagramSocket socket) {
        try {
            this.datagramSocket = socket;
            this.serverSocket =  new ServerSocket(socket.getLocalPort());
            datagramSocket.setBroadcast(true);
            //serverSocket.accept();

        } catch (IOException ex) {
            Logger.getLogger(DAODatagramSocket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }



    
    /**
     * Sends the output to the address in the specified
     * port
     *
     * @param output bytes to send
     * @param serverAdress Server adress to send to
     * @param port port to send the bytes to
     * @return true if the message has been sent
     */
    public boolean send(byte[] output, String serverAdress, int port) {
        try {
            InetAddress inetaddress = InetAddress.getByName(serverAdress);
            datagramSocket.send(new DatagramPacket(output, 0, output.length, inetaddress, port));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(DAODatagramSocket.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Sends the output to the adress in the specified
     * port
     *
     * @param output String to send
     * @param serverAdress Server adress to send to
     * @param port port to send the bytes to
     * @return true if the message has been sent
     */
    public boolean send(String output, String serverAdress, int port) {

        return send(output.getBytes(), serverAdress, port);

    }
    
    /**
     * Sends the output to the broadcast adress 255.255.255.255 in the specified
     * port
     *
     * @param output bytes to send
     * @param port port to send the bytes to
     * @return true if the message has been sent
     */
    public boolean broadcast(byte[] output, int port) {
        return send(output, "255.255.255.255", port);
    }
    
    /**
     * Sends the output to the broadcast adress 255.255.255.255 in the specified
     * port
     *
     * @param output String to send
     * @param port port to send the bytes to
     * @return true if the message has been sent
     */
    public boolean broadcast(String output, int port) {
        return send(output, "255.255.255.255", port);
    }
    
    /**
     * Sends the output to the broadcast adress 255.255.255.255 in the specified
     * port
     *
     * @param output DatagramToClone to send
     * @param port port to send the bytes to
     * @return true if the message has been sent
     */
    public boolean broadcast(DatagramPacket output, int port) {
        try {
            InetAddress adress = InetAddress.getByName("255.255.255.255");
            datagramSocket.send(new DatagramPacket(output.getData(), 0, output.getLength(), adress, port));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(DAODatagramSocket.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean send(DatagramPacket packet){
        try {
            datagramSocket.send(packet);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
 

    
    public boolean receive(DatagramPacket packet){
        try {
            datagramSocket.receive(packet);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    

    
}
