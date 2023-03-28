package src;

import java.io.IOException;
import java.net.InetAddress;
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void main(String[] args) throws IOException{
        ServerSocket serverSocket = null;
        try {
            InetAddress wifiAddress = getWifiAddress();
            if (wifiAddress != null) {
                serverSocket = new ServerSocket(1234, 50, wifiAddress);
                System.out.println("Server started on IP address: " + wifiAddress.getHostAddress());
                Server server = new Server(serverSocket);
                server.startServer();
            } else {
                System.out.println("No WiFi network found.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while starting the server: " + e.getMessage());
            if (serverSocket != null) {
                serverSocket.close();
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
    
}
