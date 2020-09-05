### Numbers Game Server - TCP Socket, Event Sourcing, CQRS, DDD
This is a multi threading application that enables multiple players to play with the computer.
You simply start with a number, each side then gets his turn to add (0,-1, or 1), number shall be 
divided by 3, and who ever reach 3/3=1 wins!

After starting the TCP socket server, clients can connect to the server and start playing automatically until they get disconnected.

#### Workflow
1. Server accepts a connection and creates a new client handler to run in a separate thread.
2. Client handler generates ID, handles streams, reads incoming commands, and registers messages listeners through the controller.
3. Controller will convert client inputs into commands and pass them to the game service.
4. The Game service executes some logic, loads the game aggregate, gets the new generated events, stores and publishes them.
5. Game’s current status is an aggregation of series of events that are stored in an event store.
6. Event bus will publish events which will be consumed by a listener to update a projection and push feedback messages to the client.
7. The events will also be consumed by a computer player (No AI or fancy stuff, just random stupid function to give you a chance to win!) 
which will react to the client’s move with new one that gets passed as a command again through the controller.

#### Description
This is a simple event driven app that quite utilizes patterns like DDD, CQRS and Event Sourcing.
It uses Java11, Google Guava, Log4J, and JUnit.
And it has 4 packages: adapters, projection, domain and service.

##### Special notes
- Short documentation provided at the header for the main classes.
- This isn’t meant to be a perfect production ready app, many things are being simply ignored.
- Only service and domain (the game core) is actually being unit/integration tested.
- Validation isn’t that sophisticated. Prospect for errors/exceptions in a number of places is ignored.

#### Build and Run

```
mvn clean package
```
```
java -jar target/numbers-game-server-0.1-jar-with-dependencies.jar
```

Once you have the server up, you can connect to it using the TCP client of your choice and start automatically playing by simply entering numbers.

Example:
```
$ nc localhost 8080
Enter a number greater or equal to 4
56
Game started. Wait for Rival's move
Your rival played 0 resulted number is 56
1
You played 1 resulted number is 19
Your rival played 1 resulted number is 19
-1
You played -1 resulted number is 6
Your rival played -1 resulted number is 6
0
You played 0 resulted number is 2
Your rival played -1 resulted number is 2
1
You played 1 resulted number is 1
You're Winner
Play new game. Enter a number greater or equal to 4
```
