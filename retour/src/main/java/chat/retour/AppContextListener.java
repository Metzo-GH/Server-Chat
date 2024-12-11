package chat.retour;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import chat.retour.Tcp.TcpServer;

import java.io.IOException;

import chat.retour.Servlet.TokenController;

@WebListener
public class AppContextListener implements ServletContextListener {
    private TcpServer tcpServer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        TokenController tokenController = TokenController.getInstance();
        tcpServer = TcpServer.getInstance();
        tcpServer.setTokenController(tokenController);
        tcpServer.startServer(12345);
        System.out.println("Application démarrée et serveur TCP lancé.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (tcpServer != null) {
            tcpServer.stopServer();
        }
        TokenController tokenController = TokenController.getInstance();
        try {
            tokenController.saveTokens();
            System.out.println("Tokens sauvegardés.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Application arrêtée.");
    }
}