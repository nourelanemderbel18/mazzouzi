package seance2;


import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class sender {
    public static void main(String[] args) {
        try {
            MulticastSocket socket = new MulticastSocket();
            InetAddress group = InetAddress.getByName("192.168.158.22"); 
            int port = 5000;

            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez le message à envoyer : ");
            String message = scanner.nextLine();
            byte[] buffer = message.getBytes();

         
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
                socket.send(packet);
                System.out.println("Message envoyé : " + message);
                Thread.sleep(1000); 
            

            socket.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
