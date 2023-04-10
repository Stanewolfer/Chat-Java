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
            InetAddress serverAddress;
            System.out.print("Enter server IP address: ");
            String ipAddress = reader.readLine().trim();
            serverAddress = InetAddress.getByName(ipAddress);
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

            // wait for input thread to complete before shutting down
            t.join();
            shutdown();
        } catch (IOException e) {
            System.err.println("An I/O error occurred while connecting to the server.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("The input thread was interrupted while waiting to join.");
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            done = true;
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (client != null && !client.isClosed()) {
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
                System.err.println("An I/O error occurred while handling user input.");
                e.printStackTrace();
                shutdown();
            }
        }

    }

    public static void main(String[] args) {
        System.out.println("Connecting to server...");
        Client client = new Client();
        client.run();
    }
}