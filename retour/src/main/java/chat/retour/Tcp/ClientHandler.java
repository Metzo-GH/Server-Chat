package chat.retour.Tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import chat.retour.Servlet.TokenController;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private TokenController tokenController;

    public ClientHandler(Socket clientSocket, TokenController tokenController) {
        this.clientSocket = clientSocket;
        this.tokenController = tokenController;
    }

    @Override
    public void run() {
        handleClient();
    }

    private void handleClient() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream()
        ) {
            out.write("Veuillez entrer votre token:\n".getBytes());
            String token = in.readLine();

            String nickname = tokenController.getTokenStore().get(token);
            if (nickname != null) {
                out.write(("Authentification réussie. Bonjour, " + nickname + "!\n").getBytes());
            } else {
                out.write("Token invalide.\n".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}