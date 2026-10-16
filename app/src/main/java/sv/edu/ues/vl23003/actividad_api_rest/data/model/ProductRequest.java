package sv.edu.ues.vl23003.actividad_api_rest.data.model;

public class ProductRequest {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;

    public ProductRequest(String nombre, String descripcion, Double precio, Integer stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }
}