package sv.edu.ues.vl23003.actividad_api_rest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sv.edu.ues.vl23003.actividad_api_rest.data.model.LoginRequest;
import sv.edu.ues.vl23003.actividad_api_rest.data.model.LoginResponse;
import sv.edu.ues.vl23003.actividad_api_rest.data.network.ApiService;
import sv.edu.ues.vl23003.actividad_api_rest.data.network.RetrofitClient;
import sv.edu.ues.vl23003.actividad_api_rest.databinding.ActivityLoginBinding;
import sv.edu.ues.vl23003.actividad_api_rest.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);


        if (sessionManager.isLoggedIn()) {
            navigateToProducts();
            return;
        }

        binding.tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String correo = binding.etCorreo.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();


        if (correo.isEmpty() || password.isEmpty()) {
            showError("Por favor completa todos los campos");
            return;
        }


        setLoading(true);


        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        LoginRequest request = new LoginRequest(correo, password);


        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    sessionManager.saveToken(loginResponse.getToken());
                    Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                    navigateToProducts();
                } else {
                    showError("Credenciales incorrectas o error del servidor");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setLoading(false);
                showError("Error de red: " + t.getMessage());
            }
        });
    }

    private void navigateToProducts() {
        Intent intent = new Intent(LoginActivity.this, ProductsActivity.class);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnLogin.setVisibility(View.GONE);
            binding.tvError.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnLogin.setVisibility(View.VISIBLE);
        }
    }

    private void showError(String message) {
        binding.tvError.setText(message);
        binding.tvError.setVisibility(View.VISIBLE);
    }
}