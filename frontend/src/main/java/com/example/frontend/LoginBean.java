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
            conn.setDoOutput(true);
            String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
            conn.getOutputStream().write(body.getBytes());

            if (conn.getResponseCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                var node = mapper.readTree(new InputStreamReader(conn.getInputStream()));
                token = node.get("token").asText();
                return "/index.xhtml?faces-redirect=true";
            }
        } catch (Exception e) {
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
