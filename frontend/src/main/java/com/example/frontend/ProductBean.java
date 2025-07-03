package com.example.frontend;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named("productBean")
@RequestScoped
public class ProductBean {
    private List<Product> products;

    @PostConstruct
    public void init() {
        loadProducts();
    }

    public List<Product> getProducts() {
        return products;
    }

    private void loadProducts() {
        products = new ArrayList<>();
        try {
            URL url = new URL("http://backend:8080/api/products");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                // Parse the JSON response manually (simple parsing for the content array)
                String jsonResponse = response.toString();
                parseProducts(jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Add some dummy products as fallback
            products.add(new Product(1L, "Error loading products", "Check backend connection", new BigDecimal("0.00"), "Error", true));
        }
    }

    private void parseProducts(String json) {
        // Simple regex-based JSON parsing for the content array
        Pattern contentPattern = Pattern.compile("\"content\":\\s*\\[([^\\]]+)\\]");
        Matcher contentMatcher = contentPattern.matcher(json);
        
        if (contentMatcher.find()) {
            String contentArray = contentMatcher.group(1);
            
            // Split by products (looking for objects between {})
            Pattern productPattern = Pattern.compile("\\{([^}]+)\\}");
            Matcher productMatcher = productPattern.matcher(contentArray);
            
            while (productMatcher.find()) {
                String productJson = productMatcher.group(1);
                Product product = parseProduct(productJson);
                if (product != null) {
                    products.add(product);
                }
            }
        }
    }

    private Product parseProduct(String productJson) {
        try {
            Long id = extractLongValue(productJson, "id");
            String name = extractStringValue(productJson, "name");
            String description = extractStringValue(productJson, "description");
            BigDecimal price = extractDecimalValue(productJson, "price");
            String category = extractStringValue(productJson, "category");
            Boolean active = extractBooleanValue(productJson, "active");

            return new Product(id, name, description, price, category, active != null ? active : true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Long extractLongValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return null;
    }

    private String extractStringValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private BigDecimal extractDecimalValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*([\\d.]+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }
        return BigDecimal.ZERO;
    }

    private Boolean extractBooleanValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(true|false)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Boolean.parseBoolean(matcher.group(1));
        }
        return null;
    }
}
