/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Remote;

import Resources.DAODatagramSocket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class Server {

    private SocketAddress address;
    private ArrayList<InetAddress> clients;
    /**
     * Lobby server
     * @param address 
     */
    public Server(SocketAddress address) {
            this.address = address;
            this.clients= new ArrayList<>();
            

    }
    
    public ArrayList<InetAddress> getClients(){
        return clients;
    }

}
