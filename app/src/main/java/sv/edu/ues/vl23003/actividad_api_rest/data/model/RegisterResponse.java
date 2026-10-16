package sv.edu.ues.vl23003.actividad_api_rest.data.model;

public class RegisterResponse {
    private Long id;
    private String nombre;
    private String correo;
    private String mensaje;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getMensaje() { return mensaje; }
}