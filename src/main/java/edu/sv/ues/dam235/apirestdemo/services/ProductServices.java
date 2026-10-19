package edu.sv.ues.dam235.apirestdemo.services;

import edu.sv.ues.dam235.apirestdemo.dtos.ProductDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ProductRequestDTO;
import java.util.List;

public interface ProductServices {
    List<ProductDTO> listar();
    ProductDTO crear(ProductRequestDTO request);
    ProductDTO actualizar(Long id, ProductRequestDTO request);
    void eliminar(Long id);
}
