package com.example.frontend;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    private String token;

    public String login() {
        try {
            URL url = new URL("http://backend:8080/api/auth/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
            conn.getOutputStream().write(body.getBytes("UTF-8"));

            int responseCode = conn.getResponseCode();
            System.out.println("Login response code: " + responseCode);
            
            if (responseCode == 200) {
                ObjectMapper mapper = new ObjectMapper();
                var node = mapper.readTree(new InputStreamReader(conn.getInputStream()));
                token = node.get("token").asText();
                System.out.println("Login successful, token received");
                return "/admin.xhtml?faces-redirect=true";
            } else {
                System.out.println("Login failed with response code: " + responseCode);
                // Read error response
                if (conn.getErrorStream() != null) {
                    var errorReader = new InputStreamReader(conn.getErrorStream());
                    var errorResponse = new java.util.Scanner(errorReader).useDelimiter("\\A").next();
                    System.out.println("Error response: " + errorResponse);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception during login: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getToken() { return token; }
}
