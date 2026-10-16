package sv.edu.ues.vl23003.actividad_api_rest.data.network;

import sv.edu.ues.vl23003.actividad_api_rest.data.model.LoginRequest;
import sv.edu.ues.vl23003.actividad_api_rest.data.model.LoginResponse;
import sv.edu.ues.vl23003.actividad_api_rest.data.model.Product;
import sv.edu.ues.vl23003.actividad_api_rest.data.model.ProductRequest;
import sv.edu.ues.vl23003.actividad_api_rest.data.model.RegisterRequest;
import sv.edu.ues.vl23003.actividad_api_rest.data.model.RegisterResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/logout")
    Call<Void> logout(@Header("Authorization") String token);

    @POST("usuarios")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @GET("productos")
    Call<List<Product>> getProducts(@Header("Authorization") String token);

    @POST("productos")
    Call<Product> createProduct(@Header("Authorization") String token, @Body ProductRequest request);

    @PUT("productos/{id}")
    Call<Product> updateProduct(@Header("Authorization") String token, @Path("id") Long id, @Body ProductRequest request);

    @DELETE("productos/{id}")
    Call<Void> deleteProduct(@Header("Authorization") String token, @Path("id") Long id);
}