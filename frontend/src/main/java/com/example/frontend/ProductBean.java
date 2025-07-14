package com.example.frontend;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Named("productBean")
@RequestScoped
public class ProductBean {
    private List<Product> products;

    @Inject
    private LoginBean loginBean;

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
            if (loginBean != null && loginBean.getToken() != null) {
                conn.setRequestProperty("Authorization", "Bearer " + loginBean.getToken());
            }

            if (conn.getResponseCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(new InputStreamReader(conn.getInputStream()));
                JsonNode content = root.path("content");
                for (JsonNode node : content) {
                    Product p = mapper.treeToValue(node, Product.class);
                    products.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            products.add(new Product(1L, "Error loading products", "Check backend connection", new BigDecimal("0.00"), "Error", true));
        }
    }
}
