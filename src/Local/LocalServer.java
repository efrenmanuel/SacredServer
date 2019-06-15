/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local;

import Resources.DAODatagramSocket;
import Resources.DAOSocket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class LocalServer implements Runnable {

    DAODatagramSocket datagramSocket;
    Socket socket;
    DAOSocket dSocket;
    ArrayList<String> clientAddresses;
    String lobbyAddress;
    int port;

    /**
     * Local Server that takes everything from the game server and distributes
     * it to the clients and emulates the clients in the network with ips from
     * 192.168.1.150 to 192.168.1.166 (max players is 66)
     *
     * @param clientAddress Address of the lobby server
     * @param port Port of the local Sacred server, Usually 2005
     */
    public LocalServer(String lobbyAddress, String serverName, int maxPlayers, int port) throws LobbyServerNotAvailable {
        try {
            String systemipaddress = "";
            datagramSocket = new DAODatagramSocket(new DatagramSocket(port));
            this.clientAddresses = new ArrayList<>();
            this.port = port;
            this.lobbyAddress = lobbyAddress;
            try {
                URL url_name = new URL("http://bot.whatismyipaddress.com");

                BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));

                // reads system IPAddress
                systemipaddress = sc.readLine().trim();
            } catch (IOException e) {
                systemipaddress = "Cannot Execute Properly";
            }
            System.out.println("Public IP Address: " + systemipaddress + "\n");
            try {
                socket = new Socket(lobbyAddress, 2004);
                DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                outToServer.writeBytes("server\n");
                System.out.println("sent server");
                outToServer.writeBytes(serverName + "INFOSEPARATOR2019" + maxPlayers + "INFOSEPARATOR2019" + 0 + "INFOSEPARATOR2019" + systemipaddress + "INFOSEPARATOR2019" + port + "\n");
                System.out.println("sent " + serverName + "INFOSEPARATOR2019" + maxPlayers + "INFOSEPARATOR2019" + 0 + "INFOSEPARATOR2019" + systemipaddress + "INFOSEPARATOR2019" + port + "\n");
            } catch (IOException ex) {
                throw new LobbyServerNotAvailable(ex);
            }
        } catch (SocketException ex) {
            Logger.getLogger(LocalServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Thread pingRelayer = new Thread(new PingRelayer(datagramSocket, clientAddresses, 2004)); //2004 port for the remote server
        pingRelayer.start();

    }

    public class LobbyServerNotAvailable extends Exception {

        public LobbyServerNotAvailable(Throwable err) {
            super("The specified server is not available", err);
        }
    }
}
