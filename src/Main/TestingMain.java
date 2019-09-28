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
                    Client client = new Client("efrenmanuel.es", 2004, 2008); //Client test, has to receive the data.

                    break;
                case 2:
                    try {
                        LocalServer local=new LocalServer("efrenmanuel.es", 2004, "testserver", 16, 2003); //Local server test, where the game server is running
                        local.run();
                    } catch (LocalServer.LobbyServerNotAvailable ex) {
                        Logger.getLogger(TestingMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;

                case 3:
                    LobbyServer lobby=new LobbyServer(true, 2004); //Lobby server test, where the lobby of servers is running
                    lobby.run();
                    break;

            }
        }

    }

}
