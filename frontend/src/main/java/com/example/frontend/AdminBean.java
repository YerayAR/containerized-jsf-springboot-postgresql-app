package com.example.frontend;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Named("adminBean")
@SessionScoped
public class AdminBean implements Serializable {
    
    private List<Product> products;
    private Product selectedProduct;
    private Product newProduct;
    private String message;
    
    @Inject
    private LoginBean loginBean;
    
    @PostConstruct
    public void init() {
        selectedProduct = new Product();
        newProduct = new Product();
        loadProducts();
    }
    
    public void loadProducts() {
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
                var root = mapper.readTree(conn.getInputStream());
                var content = root.path("content");
                for (var node : content) {
                    Product p = mapper.treeToValue(node, Product.class);
                    products.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error loading products: " + e.getMessage();
        }
    }
    
    public void createProduct() {
        try {
            URL url = new URL("http://backend:8080/api/products");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            if (loginBean != null && loginBean.getToken() != null) {
                conn.setRequestProperty("Authorization", "Bearer " + loginBean.getToken());
            }
            conn.setDoOutput(true);
            
            ObjectMapper mapper = new ObjectMapper();
            String body = mapper.writeValueAsString(newProduct);
            conn.getOutputStream().write(body.getBytes());
            
            if (conn.getResponseCode() == 200) {
                message = "Product created successfully!";
                newProduct = new Product();
                loadProducts();
            } else {
                message = "Error creating product: " + conn.getResponseMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error creating product: " + e.getMessage();
        }
    }
    
    public void updateProduct() {
        try {
            URL url = new URL("http://backend:8080/api/products/" + selectedProduct.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            if (loginBean != null && loginBean.getToken() != null) {
                conn.setRequestProperty("Authorization", "Bearer " + loginBean.getToken());
            }
            conn.setDoOutput(true);
            
            ObjectMapper mapper = new ObjectMapper();
            String body = mapper.writeValueAsString(selectedProduct);
            conn.getOutputStream().write(body.getBytes());
            
            if (conn.getResponseCode() == 200) {
                message = "Product updated successfully!";
                loadProducts();
            } else {
                message = "Error updating product: " + conn.getResponseMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error updating product: " + e.getMessage();
        }
    }
    
    public void deleteProduct(Long id) {
        try {
            URL url = new URL("http://backend:8080/api/products/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            if (loginBean != null && loginBean.getToken() != null) {
                conn.setRequestProperty("Authorization", "Bearer " + loginBean.getToken());
            }
            
            if (conn.getResponseCode() == 200) {
                message = "Product deleted successfully!";
                loadProducts();
            } else {
                message = "Error deleting product: " + conn.getResponseMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error deleting product: " + e.getMessage();
        }
    }
    
    public void selectProduct(Product product) {
        selectedProduct = new Product();
        selectedProduct.setId(product.getId());
        selectedProduct.setName(product.getName());
        selectedProduct.setDescription(product.getDescription());
        selectedProduct.setPrice(product.getPrice());
        selectedProduct.setCategory(product.getCategory());
        selectedProduct.setActive(product.isActive());
    }
    
    public void clearSelection() {
        selectedProduct = new Product();
    }
    
    // Getters and setters
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
    
    public Product getSelectedProduct() { return selectedProduct; }
    public void setSelectedProduct(Product selectedProduct) { this.selectedProduct = selectedProduct; }
    
    public Product getNewProduct() { return newProduct; }
    public void setNewProduct(Product newProduct) { this.newProduct = newProduct; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
