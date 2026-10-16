package sv.edu.ues.vl23003.actividad_api_rest.data.model;

public class RegisterRequest {
    private String nombre;
    private String correo;
    private String password;

    public RegisterRequest(String nombre, String correo, String password) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }
}