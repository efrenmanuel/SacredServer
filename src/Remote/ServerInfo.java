/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Remote;

/**
 *
 * @author efren
 */
public class ServerInfo {
    private String name;
    private int maxPlayers, currentPlayers;

    public ServerInfo(String name, int maxPlayers, int currentPlayers) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = currentPlayers;
    }

    @Override
    public String toString() {
        return "ServerInfo:\n" + "    name = " + name + "\n    maxPlayers = " + maxPlayers + "\n    currentPlayers = " + currentPlayers;
    }
            
    
}
