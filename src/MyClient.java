import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.UUID; // Pour générer un identifiant unique

public class MyClient {
    private static final int SERVER_PORT = 2112;

    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            Scanner scanner = new Scanner(System.in);

            // Générer un identifiant unique pour ce client
            String uniqueID = UUID.randomUUID().toString();

            // Demander le nom de l'utilisateur
            System.out.print("Entrez votre nom : ");
            String name = scanner.nextLine();

            // Créer un message avec le nom et l'identifiant unique
            String message = uniqueID + ":" + name;

            // Envoi du message avec l'ID unique et le nom au serveur
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), SERVER_PORT);
            clientSocket.send(packet);

            // Réception de la réponse du serveur
            byte[] responseBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            clientSocket.receive(responsePacket);
            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());

            // Affichage de la réponse du serveur
            System.out.println("Réponse du serveur : " + response);
        } catch (IOException e) {
            System.err.println("Erreur client : " + e.getMessage());
        }
    }
}
