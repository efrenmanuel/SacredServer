/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Local.LocalServer;
import Local.Client;
import Resources.DAODatagramSocket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class SecondTestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // TODO code application logic here
        startServer();
    }

    public static void startServer() {

        Thread lobby = new Thread(new Client("efrenmanuel.es", 2004));
        lobby.start();
        Thread local = new Thread(new LocalServer("efrenmanuel.es", 2003));
        local.start();

    }

}
