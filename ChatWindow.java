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
        
        // Changer la couleur de fond et de premier plan de la fenêtre
        UIManager.put("Panel.background", new Color(25, 25, 25));
        UIManager.put("OptionPane.background", new Color(25, 25, 25));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("TextField.background", new Color(40, 40, 40));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextArea.background", new Color(40, 40, 40));
        UIManager.put("TextArea.foreground", Color.WHITE);
        UIManager.put("ScrollPane.background", new Color(40, 40, 40));
        UIManager.put("ScrollBar.thumb", new Color(90, 90, 90));
        UIManager.put("ScrollBar.track", new Color(60, 60, 60));
        UIManager.put("ScrollBar.thumbDarkShadow", new Color(40, 40, 40));
        UIManager.put("ScrollBar.thumbHighlight", new Color(120, 120, 120));
        UIManager.put("ScrollBar.thumbShadow", new Color(40, 40, 40));
        
        // Ajouter les éléments de l'interface graphique ici
        JPanel chat = new JPanel();
        chat.setBackground(new Color(40, 40, 40));
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(760, 500));
        scrollPane.setBackground(new Color(50, 50, 50));
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));
        chat.add(scrollPane);
        
        JPanel envoi_mess = new JPanel();
        envoi_mess.setBackground(new Color(40, 40, 40));
        messField = new JTextField(50);
        messField.setBackground(new Color(70, 70, 70));
        messField.setForeground(Color.WHITE);
        JButton sendButton = new JButton("Send");
        envoi_mess.add(messField);
        envoi_mess.add(sendButton);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(25, 25, 25));
        mainPanel.add(chat);
        mainPanel.add(envoi_mess);
        
        add(mainPanel);        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new ChatWindow();
    }
}
