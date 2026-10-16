package sv.edu.ues.vl23003.actividad_api_rest.data.model;

public class LoginRequest {
    private String correo;
    private String password;

    public LoginRequest(String correo, String password) {
        this.correo = correo;
        this.password = password;
    }
}