import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override
    public void run() {
        try {
            Socket client = new Socket("127.0.0.1", 9999);
            out = new PrintWriter(client.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
            InputHandler inputHandler = new InputHandler();
            Thread t = new Thread(inputHandler);
            t.start();

            String inMessage;
            while ((inMessage = in.readLine()) != null) {
                System.out.println(inMessage);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown() {
        done = true;
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

    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String message = inReader.readLine();
                    if (message.equals("/quit ")) {
                        inReader.close();
                        shutdown();
                    }else {
                        out.println(message);
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }

    }
    public class ConnectionChecker {
        public static boolean checkConnection(String host, int port) {
            try {
                InetAddress.getByName(host).isReachable(5000); // Vérifie si l'hôte est atteignable
                new Socket(host, port).close(); // Vérifie si le port est ouvert
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
    public static void main(String[] args) throws UnknownHostException, IOException{
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the IP address of the server: ");
            String serverAddress = scanner.nextLine(); // l'utilisateur saisit l'adresse IP du serveur
            if (ConnectionChecker.checkConnection(serverAddress, 1234)) {
                Socket socket = new Socket(serverAddress, 1234);
                Client client = new Client();
                client.run();
            } else {
                System.out.println("Le serveur est indisponible.");
            }
        }
    }
}
