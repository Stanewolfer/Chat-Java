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
        } catch (IOException e ) {
            closeEverything(socket, bufferedReader, bufferedWriter);
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
        
    } catch (IOException e) {
        closeEverything(socket, bufferedReader, bufferedWriter);

    }

    public void public ListenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run(){
                String msgGrpFromChat;

                while (socket.isConnected()){
                    try{
                        msgGrpFromChat = bufferedReader.readLine();
                        System.out.println(msgGrpFromChat);
                    } catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }

            }
        }).start();
    }
     
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedReader)
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
    }

    public static void main(String [] args ){
        Scanner scanner = new scanner(system.in);
        system.out.println("Entrer votre nom pour le groupe de chat:");
        String username = scanner.nextLine();
        Socket socket = new Socket(host:"localhost", port: 1234 );
        Client client = new Client(socket, username );
        client.ListenForMessage();
        client.sendMessage();
        
    }

    
}


