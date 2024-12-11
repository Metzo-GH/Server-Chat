package chat.retour.Tcp;

import java.net.ServerSocket;
import java.net.Socket;

import chat.retour.Servlet.TokenController;

public class TcpServer {
    private static TcpServer instance;
    private boolean running = true;
    private ServerSocket serverSocket;
    private TokenController tokenController;

    private TcpServer() {
        // Constructeur privé pour empêcher l'instanciation directe
    }

    public static TcpServer getInstance() {
        if (instance == null) {
            instance = new TcpServer();
        }
        return instance;
    }

    public void setTokenController(TokenController tokenController) {
        this.tokenController = tokenController;
    }

    public void startServer(int port) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Serveur TCP démarré sur le port " + port);

                while (running) {
                    acceptClientConnection();
                }
            } catch (Exception e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void acceptClientConnection() {
        try {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ClientHandler(clientSocket, tokenController)).start();
        } catch (Exception e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        running = false;
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}