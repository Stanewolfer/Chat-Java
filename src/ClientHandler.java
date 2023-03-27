package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(Socket socket) throws IOException{
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try{
            String message;
            while((message = input.readLine()) != null){
                System.out.println("Received message from client: " + message);
                output.println("Echo from server: " + message);
            }
        }catch(IOException e){

        }finally{
            try{
                socket.close();
            }catch(IOException e){

            }
        }
    }

    public void close(){
        try{
            socket.close();
        }catch(IOException e){

        }
    }
}
