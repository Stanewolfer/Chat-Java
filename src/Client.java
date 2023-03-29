package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;




public class Client {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Scanner scanner;
    private String pseudo;
    private String response;

    public Client(Socket socket) throws IOException{
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        scanner = new Scanner(System.in);
        System.out.print("Enter your pseudo (max 16 characters): ");
        pseudo = scanner.nextLine();

        while(pseudo.length() == 0 || pseudo.length() > 16){
            System.out.println("Veuillez choisir un pseudo qui fait 16 caractères ou moins");
            System.out.print("Enter your pseudo (max 16 characters): ");
            pseudo = scanner.nextLine();
        }
        System.out.println("Welcome to the chat room, " + pseudo + "!");
    }

    public void start(){
        try{
            String message;
            while(true){
                System.out.print(pseudo + ": ");
                message = scanner.nextLine();
                while(message.length() > 300 || message.length() == 0){
                    System.out.println("Veuillez réecrire votre message!");
                    System.out.print(pseudo + ": ");
                    message = scanner.nextLine();
                }
            
                output.println(pseudo + ": " + message);
            }
            String response = input.readLine();
            if (response.contains(pseudo)) {
                // ignore
            } else {
                String[] splitResponse = response.split(":");
                String senderPseudo = splitResponse[0];
                String senderMessage = splitResponse[1];
                System.out.println(senderPseudo + ": " + senderMessage);
            }
        }
        finally{
            close();
        }
    }

    public void close(){
        try{
            socket.close();
        }catch(IOException e){

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
                Client client = new Client(socket);
                client.start();
            } else {
                System.out.println("Le serveur est indisponible.");
            }
        }
    }
}