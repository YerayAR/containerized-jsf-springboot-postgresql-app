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

/**
 * Managed Bean de JSF para gestionar la lógica de la vista de productos.
 * Se encarga de obtener la lista de productos desde el backend y exponerla a la vista (products.jsp).
 */
@Named("productBean") // Nombre con el que se puede acceder al bean desde las vistas JSF.
@RequestScoped      // El bean se crea para cada solicitud HTTP y se destruye al final.
public class ProductBean {

    // Lista de productos que se mostrará en la vista.
    private List<Product> products;

    // Inyección de dependencia del LoginBean para obtener el token de autenticación.
    @Inject
    private LoginBean loginBean;

    /**
     * Método de inicialización que se ejecuta después de que el bean es creado.
     * La anotación @PostConstruct asegura que este método se llame una sola vez después de la inyección de dependencias.
     */
    @PostConstruct
    public void init() {
        loadProducts();
    }

    /**
     * Getter para la lista de productos.
     * JSF utilizará este método para acceder a los productos y mostrarlos en la página.
     * @return La lista de productos.
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Carga la lista de productos desde la API del backend.
     * Realiza una solicitud HTTP GET al endpoint /api/products.
     */
    private void loadProducts() {
        products = new ArrayList<>();
        try {
            // URL del servicio de productos en el backend.
            // "backend" es el nombre del servicio definido en docker-compose.yml.
            URL url = new URL("http://backend:8080/api/products");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Si el usuario está logueado, añade el token JWT a la cabecera de autorización.
            if (loginBean != null && loginBean.getToken() != null) {
                conn.setRequestProperty("Authorization", "Bearer " + loginBean.getToken());
            }

            // Si la respuesta es exitosa (código 200 OK)
            if (conn.getResponseCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                // Lee el JSON de la respuesta.
                JsonNode root = mapper.readTree(new InputStreamReader(conn.getInputStream()));
                // El backend devuelve una estructura paginada, accedemos al array "content".
                JsonNode content = root.path("content");
                // Itera sobre cada nodo en el array de contenido y lo convierte a un objeto Product.
                for (JsonNode node : content) {
                    Product p = mapper.treeToValue(node, Product.class);
                    products.add(p);
                }
            }
        } catch (Exception e) {
            // En caso de error (ej. el backend no está disponible), se loguea la excepción
            // y se añade un producto de error a la lista para informar al usuario.
            e.printStackTrace();
            products.add(new Product(1L, "Error al cargar productos", "Verifique la conexión con el backend", new BigDecimal("0.00"), "Error", true));
        }
    }
}
