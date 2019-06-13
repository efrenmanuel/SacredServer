/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class DaoServerSocket implements Runnable {

    private ServerSocket serverSocket;
    PrintWriter out;
    BufferedReader in;
    
    public DaoServerSocket(int port){
        try {
            serverSocket= new ServerSocket(port);
            
        } catch (IOException ex) {
            Logger.getLogger(DaoServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    @Override
    public void run() {
        try {
            Socket client= serverSocket.accept();
            out = new PrintWriter(client.getOutputStream(),true);
            
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while (true) {
                System.out.println("Listening");
                System.out.println(in.readLine());
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(DaoServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
