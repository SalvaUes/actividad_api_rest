package sv.edu.ues.vl23003.actividad_api_rest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sv.edu.ues.vl23003.actividad_api_rest.data.model.RegisterRequest;
import sv.edu.ues.vl23003.actividad_api_rest.data.model.RegisterResponse;
import sv.edu.ues.vl23003.actividad_api_rest.data.network.ApiService;
import sv.edu.ues.vl23003.actividad_api_rest.data.network.RetrofitClient;
import sv.edu.ues.vl23003.actividad_api_rest.databinding.ActivityRegisterBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(v -> attemptRegister());
        binding.tvGoToLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void attemptRegister() {
        String nombre = binding.etNombre.getText().toString().trim();
        String correo = binding.etCorreo.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        if (nombre.isEmpty()) {
            showError("El nombre es obligatorio");
            return;
        }
        if (correo.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            showError("Ingresa un correo válido");
            return;
        }
        if (password.length() < 6) {
            showError("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden");
            return;
        }

        setLoading(true);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        RegisterRequest request = new RegisterRequest(nombre, correo, password);

        apiService.registerUser(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    if (response.code() == 409) {
                        showError("El correo ya está registrado");
                    } else {
                        showError("Error al crear la cuenta. Intenta nuevamente");
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                setLoading(false);
                showError("Error de red: " + t.getMessage());
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnRegister.setVisibility(View.GONE);
            binding.tvError.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnRegister.setVisibility(View.VISIBLE);
        }
    }

    private void showError(String message) {
        binding.tvError.setText(message);
        binding.tvError.setVisibility(View.VISIBLE);
    }
}