import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashMap;
import java.util.Map;

public class MyServer {
    private static final int PORT = 2112;
    private ExecutorService executor;

    // Carte pour garder la trace des clients par adresse IP, identifiant unique et nom
    private Map<String, String> clients;

    public MyServer() {
        // Utilisation d'un pool de threads pour gérer plusieurs clients
        executor = Executors.newCachedThreadPool();
        clients = new HashMap<>();
    }

    public void start() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);

            while (true) {
                // Préparation pour recevoir des données
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // Réception d'un paquet de données du client

                // Gestion de chaque client dans un thread séparé
                executor.submit(new ClientHandler(socket, packet, clients));
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur: " + e.getMessage());
        }
    }

    // Classe interne pour gérer la communication avec chaque client
    private static class ClientHandler implements Runnable {
        private DatagramSocket socket;
        private DatagramPacket packet;
        private Map<String, String> clients;

        // Constructeur qui initialise le socket, le paquet reçu, et la carte des clients
        public ClientHandler(DatagramSocket socket, DatagramPacket packet, Map<String, String> clients) {
            this.socket = socket;
            this.packet = packet;
            this.clients = clients;
        }

        @Override
        public void run() {
            try {
                // Extraction du message envoyé par le client (ID unique et nom)
                String clientData = new String(packet.getData(), 0, packet.getLength());
                String[] parts = clientData.split(":", 2);

                if (parts.length < 2) {
                    System.err.println("Données du client incorrectes !");
                    return;
                }

                String uniqueID = parts[0];
                String clientName = parts[1];

                // Récupération de l'adresse IP du client
                String clientAddress = packet.getAddress().toString();

                // Création d'une clé unique pour chaque client (IP + ID unique)
                String clientKey = clientAddress + "_" + uniqueID;

                // Ajout du client dans la carte avec son identifiant unique et son nom
                clients.put(clientKey, clientName);

                // Affichage d'un message lorsque quelqu'un se connecte
                System.out.println("Client connecté : " + clientName + " avec l'adresse IP : " + clientAddress + " et l'ID unique : " + uniqueID);

                // Envoi d'une réponse au client
                String responseMessage = "Bienvenue " + clientName + " de l'adresse IP " + clientAddress + " avec l'ID unique : " + uniqueID;
                byte[] response = responseMessage.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(response, response.length, packet.getAddress(), packet.getPort());
                socket.send(responsePacket); // Envoi de la réponse au client
            } catch (IOException e) {
                System.err.println("Erreur dans le traitement du client: " + e.getMessage());
            }
        }
    }

    // Méthode principale pour démarrer le serveur
    public static void main(String[] args) {
        MyServer server = new MyServer();
        server.start();
    }
}
