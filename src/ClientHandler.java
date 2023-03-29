package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    System.out.println(formattedDate + " " + message);
                    output.println(formattedDate + " " + message);
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
}