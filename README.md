# SacredServer
Trying to make a new lobbying system for sacred games

definitions:
  ClientServer: server running both the game server and this program in forwarder mode
  Client: Pc running the game and this program in client mode
  GameClients: both of the above
  LobbyServer: server that only forwards the game info and client info to the GameClients

Current state:

### Done
 * LobbyServer:
    - Most of it. Some things might surge along the way.
 * Client:
    - receive the server info!
    - Broadcast in the game server in the local net so the game sees it.
 * ClientServer:
    - Identify in the lobby server.
    - Send the broadcast info to the clients connected.

### Need to do:
 * LobbyServer:
    - None
 * Client:
    - add switch to broadcast or not (only one should broadcast per real net even with multiple computers playing)
    - add functionality to receive tcp game packets (Game data)
    -add  functionality to send tcp packets back to the ClientServer
   
 * ClientServer:
    - Send game packets to the client 
      *emulate every Client with ips from (Probably personalizable?) 192.168.1.150 to .165 (50 + 16 max client (game cap)) and redirect the data back to the real client's ip and port identifier
    
### notes
I also have the suspicion that the code might be reusable to make lobbies of a lot of other games that only allow for lan games since it looks like a pretty much simple behavior
