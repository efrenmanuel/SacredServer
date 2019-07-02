/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class Forwarder extends Thread {

    private static final int READ_BUFFER_SIZE = 8192;

    InputStream input;
    OutputStream output;
    String entry;
    ConcurrentHashMap<String, Socket> collection;
    ConcurrentHashMap<String, Socket> collection2;

    /**
     * Creates a new traffic forward thread specifying its input stream, output
     * stream and parent thread
     * @param collection Collection that the input comes from
     * @param collection2 Collection that contains the output
     * @param entry Name of the input and output in both collection
     */
    public Forwarder(ConcurrentHashMap<String, Socket> collection, ConcurrentHashMap<String, Socket> collection2, String entry) {
        try {
            Socket inputSocket = collection.get(entry);
            Socket outputSocket = collection2.get(entry);
            this.input = inputSocket.getInputStream();
            this.output = outputSocket.getOutputStream();
            this.collection = collection;
            this.collection2 = collection2;
        } catch (IOException ex) {
            Logger.getLogger(Forwarder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Runs the thread. Until it is possible, reads the input stream and puts
     * read data in the output stream. If reading can not be done (due to
     * exception or when the stream is at his end) or writing is failed, exits
     * the thread.
     */
    @Override
    public void run() {
        byte[] buffer = new byte[READ_BUFFER_SIZE];
        try {
            while (true) {
                int bytesRead = input.read(buffer);
                if (bytesRead == -1) {
                    break; // End of stream is reached --> exit the thread
                }
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // Read/write failed --> connection is broken --> exit the thread
        }
        
        collection.remove(entry);
        collection2.remove(entry);

    }
}
