package edu.sv.ues.dam235.apirestdemo.controllers;

import edu.sv.ues.dam235.apirestdemo.dtos.ProductDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ProductRequestDTO;
import edu.sv.ues.dam235.apirestdemo.services.ProductServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServices productServices;

    @GetMapping
    public List<ProductDTO> listar() {
        return productServices.listar(); // lista vacía = 200 con []
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO crear(@Valid @RequestBody ProductRequestDTO request) {
        return productServices.crear(request);
    }

    @PutMapping("/{id}")
    public ProductDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {
        return productServices.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        productServices.eliminar(id);
    }
}
