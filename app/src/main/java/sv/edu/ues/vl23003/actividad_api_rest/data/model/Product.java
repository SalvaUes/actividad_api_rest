package sv.edu.ues.vl23003.actividad_api_rest.data.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    private Long id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("precio")
    private Double precio;

    @SerializedName("stock")
    private Integer stock;

    @SerializedName("activo")
    private Boolean activo;


    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }
    public Integer getStock() { return stock; }
    public Boolean getActivo() { return activo; }
}