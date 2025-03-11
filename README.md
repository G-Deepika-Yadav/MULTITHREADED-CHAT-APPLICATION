# MULTITHREADED-CHAT-APPLICATION
*COMPANY* : CODTECH IT SOLUTIONS

*NAME* : G DEEPIKA

*INTERN ID*: CT6WQZA

*DOMAIN*: JAVA PROGRAMMING

*DURATION* : 6 WEEKS

*MENTOR*: NEELA SANTHOSH


*DESCRIPTION*:
Server-Side Functionality (ChatServer):

The ChatServer class initializes a ServerSocket on a specified port (12345 in this case), listening for incoming client connections. Upon accepting a new connection, the server creates a ClientHandler thread to manage communication with that client. This multi-threaded approach allows the server to handle multiple clients concurrently, ensuring real-time communication.

The ClientHandler thread is responsible for reading messages from the client, processing them, and broadcasting them to other connected clients. It uses BufferedReader and PrintWriter to handle text-based communication over the socket. When a client connects, the ClientHandler reads the client's name, which is then used to identify the client in subsequent messages. The server maintains a HashSet called clientWriters to keep track of the PrintWriter objects associated with each connected client. This set is used to efficiently broadcast messages to all clients.

The broadcast() method iterates through the clientWriters set and sends the message to each client, excluding the sender. This ensures that a client doesn't receive its own messages back. The addWriter() and removeWriter() methods are synchronized to prevent race conditions when multiple threads try to modify the clientWriters set simultaneously. Upon client disconnection, the ClientHandler removes the client's PrintWriter from the set and broadcasts a disconnection message.

Client-Side Functionality (ChatClient):

The ChatClient class establishes a connection to the server using a Socket. It prompts the user to enter their name, which is then sent to the server. The client uses two threads: one for reading messages from the server and another for sending messages to the server.

The receiving thread continuously reads messages from the server using a BufferedReader and displays them on the console. This allows the client to receive and display messages from other clients in real-time. The sending thread reads user input from the console using another BufferedReader and sends it to the server using a PrintWriter. This allows the client to send messages to other clients.

Key Design Considerations:

Multi-threading: The use of threads is crucial for handling multiple clients concurrently. Without threads, the server would be blocked while waiting for a single client, preventing other clients from connecting or communicating.
Sockets: Sockets provide a reliable and efficient way to establish network connections between the server and clients.
Input/Output Streams: BufferedReader and PrintWriter are used for efficient text-based communication over the sockets.
Synchronization: Synchronization is essential to prevent race conditions when multiple threads access shared resources, such as the clientWriters set.
Error Handling: Basic error handling is implemented to handle client disconnections and other potential issues. However, more robust error handling would be necessary for a production-ready application.
Client Name: the client now sends its name to the server, and the server displays the name with each message. This enhances the user experience by providing clear identification of message senders.
Clear Separation of Concerns: The code is well-structured, with separate classes for the server and client, promoting code reusability and maintainability.
