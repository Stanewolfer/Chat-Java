import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;

    private Server(ServerSocket serverSocket) {
        connections = new ArrayList<>();
        done = false;
        server = serverSocket;
    }

    @Override
    public void run() {
        try {
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client  = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void broadcast(String message){
        for (ConnectionHandler ch : connections) {
            if (ch != null) {
                ch.sendMessage(message);
            }
        }
    }

    public void shutdown() {
        try {
            done = true;
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler ch : connections) {
                ch.shutdown();
            }
        } catch (IOException e) {
            // ignore
        }

    }

    class ConnectionHandler implements Runnable {
        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;
        public ConnectionHandler(Socket client){
            this.client = client;
        }
        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(),true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Enter a nickname: ");
                nickname = in.readLine();
                System.out.println(nickname + " connected!");
                broadcast(nickname + " has joined the server!");
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/rename ")) {
                        String[] messageSplit = message.split(" ", 2);
                        if (messageSplit.length == 2) {
                            broadcast(nickname + " is now known as: " + messageSplit[1]);
                            System.out.println(nickname + " is now known as: " + messageSplit[1]);
                            nickname = messageSplit[1];
                            out.println("Successfully changed nickname to " + nickname + "!");
                        }else {
                            out.println("No nickname provided!");
                        }
                    }else if (message.startsWith("/quit ")) {
                        broadcast(nickname + " has left the chat!");
                        shutdown();
                    }else if (message.equals("/help")) {
                        out.println("Available commands: /rename <new_nickname>, /quit, /help");
                    }else {
                        broadcast(nickname + ": " + message);
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }
        public void sendMessage(String message) {
            out.println(message);
        }
        public void shutdown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()){
                    client.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting server...");
        ServerSocket serverSocket = new ServerSocket(9999);
        String ipAddress = "http://localhost";
        System.out.println("Server running on IP address " + ipAddress + ", port 9999");
        Server server = new Server(serverSocket);
        new Thread(server).start();
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        String message;
        while ((message = console.readLine()) != null) {
            if (message.equals("/quit")) {
            server.shutdown();
            break;
            }else if (message.equals("/help")) {
            System.out.println("Available commands: /quit, /help");
            }else {
            System.out.println("Unknown command. Type /help for a list of available commands.");
            }
        }
    }
}