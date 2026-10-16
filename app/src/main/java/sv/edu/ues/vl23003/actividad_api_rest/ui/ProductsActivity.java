package sv.edu.ues.vl23003.actividad_api_rest.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import sv.edu.ues.vl23003.actividad_api_rest.data.model.Product;
import sv.edu.ues.vl23003.actividad_api_rest.data.model.ProductRequest;
import sv.edu.ues.vl23003.actividad_api_rest.data.network.ApiService;
import sv.edu.ues.vl23003.actividad_api_rest.data.network.RetrofitClient;
import sv.edu.ues.vl23003.actividad_api_rest.databinding.ActivityProductsBinding;
import sv.edu.ues.vl23003.actividad_api_rest.databinding.DialogProductBinding;
import sv.edu.ues.vl23003.actividad_api_rest.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity implements ProductAdapter.OnProductActionListener {

    private ActivityProductsBinding binding;
    private SessionManager sessionManager;
    private ProductAdapter adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        if (!sessionManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }

        setupRecyclerView();
        setupListeners();
        loadProducts();
    }

    private void setupRecyclerView() {
        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        binding.btnLogout.setOnClickListener(v -> performLogout());
        binding.fabAddProduct.setOnClickListener(v -> showCreateProductDialog());
    }

    private void loadProducts() {
        setLoading(true);
        String token = sessionManager.getToken();

        apiService.getProducts(token).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();
                    if (products.isEmpty()) {
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                        binding.recyclerProducts.setVisibility(View.GONE);
                    } else {
                        binding.tvEmpty.setVisibility(View.GONE);
                        binding.recyclerProducts.setVisibility(View.VISIBLE);
                        adapter = new ProductAdapter(products, ProductsActivity.this);
                        binding.recyclerProducts.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(ProductsActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                    if (response.code() == 401) {
                        sessionManager.clearSession();
                        navigateToLogin();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                setLoading(false);
                Toast.makeText(ProductsActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEdit(Product product) {
        showEditProductDialog(product);
    }

    @Override
    public void onDelete(Product product) {
        showDeleteConfirmationDialog(product);
    }

    private void showCreateProductDialog() {
        DialogProductBinding dialogBinding = DialogProductBinding.inflate(LayoutInflater.from(this));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Producto");
        builder.setView(dialogBinding.getRoot());

        builder.setPositiveButton("Crear", (dialog, which) -> {
            String nombre = dialogBinding.etDialogNombre.getText().toString().trim();
            String descripcion = dialogBinding.etDialogDescripcion.getText().toString().trim();
            String precioStr = dialogBinding.etDialogPrecio.getText().toString().trim();
            String stockStr = dialogBinding.etDialogStock.getText().toString().trim();

            if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Double precio = Double.parseDouble(precioStr);
                Integer stock = Integer.parseInt(stockStr);
                ProductRequest request = new ProductRequest(nombre, descripcion, precio, stock);
                createProduct(request);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Precio o stock inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void showEditProductDialog(Product product) {
        DialogProductBinding dialogBinding = DialogProductBinding.inflate(LayoutInflater.from(this));

        dialogBinding.etDialogNombre.setText(product.getNombre());
        dialogBinding.etDialogDescripcion.setText(product.getDescripcion());
        dialogBinding.etDialogPrecio.setText(String.valueOf(product.getPrecio()));
        dialogBinding.etDialogStock.setText(String.valueOf(product.getStock()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Producto");
        builder.setView(dialogBinding.getRoot());

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = dialogBinding.etDialogNombre.getText().toString().trim();
            String descripcion = dialogBinding.etDialogDescripcion.getText().toString().trim();
            String precioStr = dialogBinding.etDialogPrecio.getText().toString().trim();
            String stockStr = dialogBinding.etDialogStock.getText().toString().trim();

            if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Double precio = Double.parseDouble(precioStr);
                Integer stock = Integer.parseInt(stockStr);
                ProductRequest request = new ProductRequest(nombre, descripcion, precio, stock);
                updateProduct(product.getId(), request);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Precio o stock inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void showDeleteConfirmationDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Producto");
        builder.setMessage("¿Estás seguro de eliminar \"" + product.getNombre() + "\"?");

        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            deleteProduct(product.getId());
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void createProduct(ProductRequest request) {
        setLoading(true);
        String token = sessionManager.getToken();

        apiService.createProduct(token, request).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(ProductsActivity.this, "Producto creado", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(ProductsActivity.this, "Error al crear producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                setLoading(false);
                Toast.makeText(ProductsActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProduct(Long productId, ProductRequest request) {
        setLoading(true);
        String token = sessionManager.getToken();

        apiService.updateProduct(token, productId, request).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(ProductsActivity.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(ProductsActivity.this, "Error al actualizar producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                setLoading(false);
                Toast.makeText(ProductsActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProduct(Long productId) {
        setLoading(true);
        String token = sessionManager.getToken();

        apiService.deleteProduct(token, productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(ProductsActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(ProductsActivity.this, "Error al eliminar producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                setLoading(false);
                Toast.makeText(ProductsActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performLogout() {
        String token = sessionManager.getToken();

        apiService.logout(token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                sessionManager.clearSession();
                navigateToLogin();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sessionManager.clearSession();
                navigateToLogin();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.recyclerProducts.setVisibility(View.GONE);
            binding.tvEmpty.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}