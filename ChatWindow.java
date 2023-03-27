import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.*;
import javax.swing.*;

public class ChatWindow extends JFrame {
    
    private JTextArea chatArea;
    private JTextField messField;
    
    public ChatWindow() {
        setTitle("Chat Window");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Ajouter les éléments de l'interface graphique ici
        JPanel chat = new JPanel();
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(760, 500));
        chat.add(scrollPane);
        
        JPanel envoi_mess = new JPanel();
        messField = new JTextField(50);
        JButton sendButton = new JButton("Send");
        envoi_mess.add(messField);
        envoi_mess.add(sendButton);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(chat);
        mainPanel.add(envoi_mess);
        
        add(mainPanel);
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new ChatWindow();
    }
}
