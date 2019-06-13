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
public class DAOSocket {
    
    private Socket clientSocket;
    PrintWriter writer;
    
    /**
     * Class to make using sockets a little easier on me
     * @param serverAddress The local address of the listening socket
     * @param port The port to listen to
     */
    public DAOSocket(String serverAddress, int port){
        try {
            clientSocket= new Socket(InetAddress.getByName(serverAddress), port);
            OutputStream output= clientSocket.getOutputStream();
            writer = new PrintWriter(output, true);
            
            
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(DAOSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DAOSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean send(String message){
        writer.println(message);
        return true;
    }
}
