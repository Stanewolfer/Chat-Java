import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            InetAddress serverAddress = InetAddress.getLocalHost();
            System.out.println("Server IP address: " + serverAddress.getHostAddress());
            System.out.print("Enter server IP address (press enter to use local address): ");
            String ipAddress = reader.readLine().trim();
            if (!ipAddress.isEmpty()) {
                serverAddress = InetAddress.getByName(ipAddress);
            }
            client = new Socket(serverAddress, 9999);
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

    public static void main(String[] args) throws IOException {
        System.out.println("Connecting to server...");
        Client client = new Client();
        client.run();
    }
}
