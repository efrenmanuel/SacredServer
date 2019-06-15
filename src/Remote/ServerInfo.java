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
    private String ip;
    private int port;

    public ServerInfo(String name, int maxPlayers, int currentPlayers, String ip, int port) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = currentPlayers;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServerInfo:\n" + "    name = " + name + "\n    maxPlayers = " + maxPlayers + "\n    currentPlayers = " + currentPlayers + "\n    ip = " + ip + "\n    port = " + port;
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}
