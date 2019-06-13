# SacredServer
Trying to make a new lobbying system for sacred games

definitions:
  ClientServer: server running both the game server and this program in forwarder mode
  Client: Pc running the game and this program in client mode
  GameClients: both of the above
  LobbyServer: server that only forwards the game info and client info to the GameClients

Current state:
```
  Can send the ping from a main server (the same as the gameserver to the clients)
  Lobby server -> none
  Client -> Can receive the ping from the real server and broadcast it
```

### Need to do:
 * LobbyServer:
    - Complete. informs the clients of the available servers and the server of the clients it needs to send info too
 * Client:
    - add switch to broadcast or not (only one should broadcast per real net even with multiple computers playing)
    - add functionality to receive game packets (Game data) 
    - add functionality to send the LobbyServer its ip public and port
        *?each packet will have to include a (4 bits) identifier of the client and we will only need one main client per net
    -add  functionality to send tcp packets back to the ClientServer
   
 * ClientServer:
    - Send game packets to the client with its (4 bit) identifier and receive back
      *emulate every Client with ips from (Probably personalizable?) 192.168.1.150 to .165 (50 + 16 max client (game cap)) and redirect the data back with its corresponding (4 bit) identifier
    
### notes
I also have the suspicion that the code might be reusable to make lobbies of a lot of other games that only allow for lan games since it looks like a pretty much simple behavior
