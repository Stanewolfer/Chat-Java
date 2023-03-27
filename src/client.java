package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket; 

public class client {

    private Socket socket; 
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public client(Socket socket, String username ){
        try{
            this.socket = socket; 
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username; 
        }catch (IOException e ){
            closeEverything(socket, )
        }
    }
}
