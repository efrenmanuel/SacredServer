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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class LocalServer implements Runnable {

    DAODatagramSocket datagramSocket;
    Socket socket;
    String lobbyAddress;
    int port;

    /**
     *
     * @param lobbyAddress Address of the lobby server
     * @param port Port of the local Sacred server, Usually 2005
     */
    public LocalServer(String lobbyAddress, int port) {
        try {

            datagramSocket = new DAODatagramSocket(new DatagramSocket(port));
            this.lobbyAddress=lobbyAddress;
            this.port=port;
        } catch (SocketException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void run() {
        Thread pingRelayer = new Thread(new PingRelayer(datagramSocket, lobbyAddress, 2004)); //2004 port for the remote server
        pingRelayer.start();

    }

}
