/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Local.Server.LocalServer;
import Local.ClientPackage.Client;
import Remote.LobbyServer;
import Resources.DAOSocket;
import Resources.DAODatagramSocket;
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
            System.out.println("Input type of socket to test:\n 1.-Client\n 2.-Server\n 3.- Lobby");
            switch (scan.nextInt()) {
                case 1:
                    Thread client = new Thread(new Client("efrenmanuel.es", 2003)); //Client test, has to receive the data.
                    client.start();

                    break;
                case 2:
                    try {
                        Thread local = new Thread(new LocalServer("efrenmanuel.es", "testserver", 16, 2003)); //Local server test, where the game server is running
                        local.start();
                    } catch (LocalServer.LobbyServerNotAvailable ex) {
                        Logger.getLogger(TestingMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;

                case 3:
                    Thread lobby = new Thread(new LobbyServer(2004)); //Lobby server test, where the lobby of servers is running
                    lobby.start();
                    break;

            }
        }

    }

}
