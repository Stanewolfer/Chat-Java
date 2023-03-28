package src;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ip {
  public static void main(String[] args) {
    String ipAddress = "";
    try {
      // Obtient l'adresse IP de la machine locale
      InetAddress inetAddress = InetAddress.getLocalHost();
      ipAddress = inetAddress.getHostAddress();
    } catch (UnknownHostException ex) {
      // Gestion d'exception
      ex.printStackTrace();
    }
    System.out.println("Adresse IP: " + ipAddress);
  }
}
