# SnowBooks-Server
A simple server to handle data processing and storing for the Snow-Books client

### How It Works
The server opens a socket on the defined port and waits for connections from the client. The client makes a connection every time it wants
to transmit data and the server is always ready to process it. Once the data is processed the connection diminishes.

### Features
- Offers a simple console JavaFX application that can easily take in new commands. Currently only 1 command is useful.
- Processes packets in a multi-threaded fashion.
- Stores active users and their current ip.
- Stores User accounts using serialization.

### My Experience
I've created a lot of small server / client applications so this was a walk in the park for me. I did want to create something interesting in this project so thats where the console comes into play. I only spent about 1 - 2 days on it so its not necessarily functional. I used it to spoof transactions so I could represent a whole years worth of data. I had other plans for it but this project was built quickly to represent my abilities.
