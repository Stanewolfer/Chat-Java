package src;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public static void sendMessage(String string){
        try{

            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            //efface toutes les données mises en mémoire tampon dans un objet BufferedWriter vers le flux sous-jacent.
    
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write( username + ":" + messageToSend );
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }  catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void ListenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run(){
                String msgFromGroupChat;

                while (socket.isConnected()){
                    try{
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public static void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
    }

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name for the chat group: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username);
        client.ListenForMessage();;
        client.sendMessage(username);

    }
}

