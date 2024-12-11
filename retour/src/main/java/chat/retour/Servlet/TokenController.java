package chat.retour.Servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import chat.retour.Tcp.TcpServer;

@WebServlet("/api/tokens/*")
public class TokenController extends HttpServlet {
    private static TokenController instance;
    private static final String DATA_FILE = "tokens.json";
    private Map<String, String> tokenStore = new HashMap<>();

    public TokenController() {
        instance = this;
        try {
            loadTokens();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TokenController getInstance() {
        return instance;
    }

    public Map<String, String> getTokenStore() {
        return tokenStore;
    }

    public void saveTokens() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(DATA_FILE), tokenStore);
    }

    public void loadTokens() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(DATA_FILE);
        if (file.exists()) {
            tokenStore = mapper.readValue(file, new TypeReference<Map<String, String>>() {});
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if ("/register".equals(path)) {
            String token = req.getParameter("token");
            String nickname = req.getParameter("nickname");
            tokenStore.put(token, nickname);
            saveTokens();
            resp.getWriter().write("Token et nickname enregistrés avec succès.");
        } else if ("/stop-server".equals(path)) {
            TcpServer tcpServer = TcpServer.getInstance();
            tcpServer.stopServer();
            saveTokens();
            resp.getWriter().write("Serveur TCP arrêté.");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if ("/check".equals(path)) {
            String token = req.getParameter("token");
            String nickname = tokenStore.get(token);
            if (nickname != null) {
                resp.getWriter().write("Nickname associé : " + nickname);
            } else {
                resp.getWriter().write("Token non trouvé.");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}