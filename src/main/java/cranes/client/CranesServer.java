package cranes.client;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpsConfigurator;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CranesServer {
    private static Connection dbConnection;

    public static void startServer() throws Exception {
        initDatabase();
        HttpsServer server = HttpsServer.create(new InetSocketAddress(8443), 0);
        SSLContext sslContext = createSSLContext();
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext));
        server.createContext("/check", new CheckHandler());
        server.createContext("/purchase", new PurchaseHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Сервер запущен на порту 8443");
    }

    private static void initDatabase() throws SQLException {
        dbConnection = DriverManager.getConnection("jdbc:sqlite:subscriptions.db");
        Statement stmt = dbConnection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS subscriptions (userId TEXT PRIMARY KEY, isSubscribed BOOLEAN, expiryDate TEXT)");
        stmt.close();
    }

    private static SSLContext createSSLContext() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream("keystore.jks"), "password".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, "password".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return sslContext;
    }

    public record SubscriptionStatus(boolean isSubscribed, String expiryDate) {
    }

    public static SubscriptionStatus checkSubscription(String userId) {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement("SELECT isSubscribed, expiryDate FROM subscriptions WHERE userId = ?");
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean isSubscribed = rs.getBoolean("isSubscribed");
                String expiryDate = rs.getString("expiryDate");
                LocalDateTime expiry = LocalDateTime.parse(expiryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (LocalDateTime.now().isAfter(expiry)) {
                    isSubscribed = false;
                    updateSubscription(userId, false, expiryDate);
                }
                return new SubscriptionStatus(isSubscribed, expiryDate);
            } else {
                return new SubscriptionStatus(false, "N/A");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new SubscriptionStatus(false, "N/A");
        }
    }

    public static SubscriptionStatus purchaseSubscription(String userId) {
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(30);
        String expiryDateStr = expiryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try {
            updateSubscription(userId, true, expiryDateStr);
            return new SubscriptionStatus(true, expiryDateStr);
        } catch (SQLException e) {
            e.printStackTrace();
            return new SubscriptionStatus(false, "N/A");
        }
    }

    private static void updateSubscription(String userId, boolean isSubscribed, String expiryDate) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement("INSERT OR REPLACE INTO subscriptions (userId, isSubscribed, expiryDate) VALUES (?, ?, ?)");
        stmt.setString(1, userId);
        stmt.setBoolean(2, isSubscribed);
        stmt.setString(3, expiryDate);
        stmt.executeUpdate();
        stmt.close();
    }

    static class CheckHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String userId = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).readLine();
            SubscriptionStatus status = checkSubscription(userId);
            String response = status.isSubscribed() + "," + status.expiryDate();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class PurchaseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String userId = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).readLine();
            SubscriptionStatus status = purchaseSubscription(userId);
            String response = status.isSubscribed() + "," + status.expiryDate();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}