import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
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
        } catch (Exception e) {
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
                out.println("Entrer un pseudo : ");
                nickname = in.readLine();
                System.out.println(nickname + " est connecté !");
                broadcast(nickname + " a rejoint le serveur !");
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/nick ")) {
                        String[] messageSplit = message.split(" ", 2);
                        if (messageSplit.length == 2) {
                            broadcast(nickname + " se renommer en : " + messageSplit[1]);
                            System.out.println(nickname + " se renommer en  : " + messageSplit[1]);
                            nickname = messageSplit[1];
                            out.println("La modification du pseudo en " + nickname + "est un succès !");
                        }else {
                            out.println("No nickname provided !");
                        }
                    }else if (message.startsWith("/quit ")) {
                        broadcast(nickname + " à quitter la discussion !");
                        shutdown();
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
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
    }
    
    private static InetAddress getWifiAddress() throws IOException {
        InetAddress wifiAddress = null;
        InetAddress localhost = InetAddress.getLocalHost();
        InetAddress[] allAddresses = InetAddress.getAllByName(localhost.getHostName());
        for (InetAddress address : allAddresses) {
            if (address.isSiteLocalAddress() && !address.equals(localhost)) {
                wifiAddress = address;
                break;
            }
        }
        return wifiAddress;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            InetAddress wifiAddress = getWifiAddress();
            if (wifiAddress != null) {
                serverSocket = new ServerSocket(1234, 50, wifiAddress);
                System.out.println("Server started on IP address: " + wifiAddress.getHostAddress());
                Server server = new Server(serverSocket);
                server.run();
            } else {
                System.out.println("No WiFi network found.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while starting the server: " + e.getMessage());
        }
    }
}
