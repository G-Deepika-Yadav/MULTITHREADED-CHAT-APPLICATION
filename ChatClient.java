import java.io.*;
import java.net.*;
import java.util.*;

// Server Class
class ChatServer {

    private static final int PORT = 12345;
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Chat Server started on port " + PORT);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } finally {
            serverSocket.close();
        }
    }

    static void broadcast(String message, PrintWriter sender) {
        for (PrintWriter writer : clientWriters) {
            if (writer != sender) { // Avoid sending the message back to the sender
                writer.println(message);
                writer.flush();
            }
        }
    }

    static synchronized void addWriter(PrintWriter writer) {
        clientWriters.add(writer);
    }

    static synchronized void removeWriter(PrintWriter writer) {
        clientWriters.remove(writer);
    }

    // Client Handler Class (Thread)
    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter writer;
        private BufferedReader reader;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
                addWriter(writer);

                String clientName = reader.readLine(); // Read client name
                System.out.println(clientName + " has joined the chat.");
                broadcast(clientName + " has joined the chat.", writer);

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println(clientName + ": " + message);
                    broadcast(clientName + ": " + message, writer);
                }
            } catch (IOException e) {
                // Client disconnected
            } finally {
                try {
                    if (writer != null) {
                        removeWriter(writer);
                        broadcast("A client has left the chat.", writer);
                    }
                    if (reader != null) {
                        reader.close();
                    }
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// Client Class
class ChatClient {

    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost"; // Or your server IP
        int serverPort = 12345;

        Socket socket = new Socket(serverAddress, serverPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter your name: ");
        String name = consoleReader.readLine();
        out.println(name); // Send client name to the server

        // Thread to read messages from the server
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Thread to send messages to the server
        String userInput;
        while ((userInput = consoleReader.readLine()) != null) {
            out.println(userInput);
        }
    }
}