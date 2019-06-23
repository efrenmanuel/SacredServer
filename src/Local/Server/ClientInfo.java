/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Local.Server;

import Remote.*;

/**
 *
 * @author efren
 */
public class ClientInfo {

    private String name;

    public ClientInfo(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServerInfo:\n" + "    name = " + name;
    }

    public String getName() {
        return name;
    }

}
