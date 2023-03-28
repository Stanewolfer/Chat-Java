package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        Server.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while(!serverSocket.isClosed()){
                Socket client = serverSocket.accept();
                System.out.println("A new client has connected !");
                ClientHandler clientHandler = new ClientHandler(client);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch(IOException e){
            System.err.println("An error occurred while communicating with the server: " + e.getMessage());
        }
    }

    public void closeServerSocket(){
        try {
            if(serverSocket != null ){
                serverSocket.close();
                System.out.println("Server socket closed.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while closing the server socket: " + e.getMessage());
        }
    }
    

    public void main(String[] args) throws IOException{
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            Server server = new Server(serverSocket);
            System.out.println("Server started !");
            server.startServer();
        } catch (IOException e) {
            System.err.println("An error occurred while starting the server: " + e.getMessage());
        }
    }
}
