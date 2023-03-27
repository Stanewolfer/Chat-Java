package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner; 

public class Client {

    private Socket socket; 
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username ){
        try{
            this.socket = socket; 
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username; 
        }catch (IOException e ){
            CloseEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(){
        bufferedWriter.write(username);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        //efface toutes les données mises en mémoire tampon dans un objet BufferedWriter vers le flux sous-jacent.

        Scanner scanner = new Scanner(system.in);
        while (socket.isConnected()) {
            String messageToSend = scanner.nextLine();
            bufferedWriter.write( str: username + ":" + messageToSend );
            bufferedWriter.newLine();
        }   bufferedWriter.flush();
        
    }

}
