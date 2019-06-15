/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local;

import Remote.ServerInfo;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efren
 */
public class ServerListUpdater implements Runnable {

    private Socket socket;
    private Map<String, ServerInfo> serverList;

    public ServerListUpdater(Socket socket, Map<String, ServerInfo> serverList) {
        this.socket = socket;
        this.serverList = serverList;

    }

    @Override
    public void run() {
        DataOutputStream outToServer = null;
        try {
            outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes("client\n");
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String server;
            System.out.println("Asking the lobby for available servers");
            while ((server = input.readLine()) != null) {
                System.out.println("found a server");
                String[] serverData = server.split("INFOSEPARATOR2019");
                ServerInfo serverInfo = new ServerInfo(serverData[0], Integer.parseInt(serverData[1]), Integer.parseInt(serverData[2]),serverData[3],Integer.parseInt(serverData[4]));
                serverList.put(serverData[3]+":"+serverData[4], serverInfo);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerListUpdater.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outToServer.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerListUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
