package src;

import java.io.BufferedReader;
import java.io.FileWriter;
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
                    System.out.println("Veuillez réecrire votre message!r");
                    System.out.print(pseudo + ": ");
                    message = scanner.nextLine();
                    }
            
                output.println(pseudo + ": " + message);
                String response = input.readLine();
                if (response.contains(pseudo)) {
                	// ignore
                } else {
                	System.out.println(response);
                }
            }
        }catch(IOException e){

        }finally{
            close();
        }
    }

    public void close(){
        try{
            socket.close();
        }catch(IOException e){

        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        InetAddress wifiAddress = getWifiAddress();
        if (wifiAddress != null) {
            Socket socket = new Socket(wifiAddress, 1234);
            System.out.println("Connected to server at IP address: " + wifiAddress.getHostAddress());
            Client client = new Client(socket);
            client.start();
        } else {
            System.out.println("No WiFi network found.");
        }
    }
    
    private static InetAddress getWifiAddress() throws UnknownHostException {
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

}