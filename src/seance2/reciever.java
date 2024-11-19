package seance2;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class reciever {
    public static void main(String[] args) {
        try {
            int port = 5000;
            InetAddress group = InetAddress.getByName("192.168.158.22"); 
            MulticastSocket socket = new MulticastSocket(port);
            socket.joinGroup(group);

            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String receivedMessage = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Message reçu : " + receivedMessage);

            socket.leaveGroup(group);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}