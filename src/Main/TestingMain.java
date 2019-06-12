/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Local.LocalServer;
import Remote.Client;
import Resources.DAOSocket;
import Resources.DAODatagramSocket;
import Resources.DaoServerSocket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class TestingMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Input type of socket to test:\n 1.-Client\n 2.-Server");
            switch (scan.nextInt()) {
                case 1:
                    Thread client = new Thread(new Client("efrenmanuel.es", 2004));
                    client.start();
                    while (true) {                        
                        
                    }
                    //break;
                case 2:
                    Thread local = new Thread(new LocalServer("efrenmanuel.es", 2005));
                    local.start();
                    break;

            }
        }

    }

}
