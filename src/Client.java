package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        System.out.print("Enter your pseudo: ");
        pseudo = scanner.nextLine();
        System.out.println("Welcome to the chat room, " + pseudo + "!");
    }

    public void start(){
        try{
            String message;
            while(true){
                System.out.print(pseudo + ": ");
                message = scanner.nextLine();
                output.println(pseudo + ": " + message);
                String response = input.readLine();
                System.out.println("Received response from server: " + response);
            }
        }catch(IOException e){

        }finally{
            try{
                socket.close();
            }catch(IOException e){

            }
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException{
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket);
        client.start();
    }
}
