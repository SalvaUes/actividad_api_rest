package edu.sv.ues.dam235.apirestdemo.implementations;

import edu.sv.ues.dam235.apirestdemo.dtos.ProductDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ProductRequestDTO;
import edu.sv.ues.dam235.apirestdemo.entities.Producto;
import edu.sv.ues.dam235.apirestdemo.repositories.ProductoRepository;
import edu.sv.ues.dam235.apirestdemo.services.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServicesImpl implements ProductServices {

    private final ProductoRepository productoRepository;

    @Override
    public List<ProductDTO> listar() {
        return productoRepository.findAll(Sort.by("id")).stream().map(this::toDTO).toList();
    }

    @Override
    public ProductDTO crear(ProductRequestDTO request) {
        Producto p = new Producto();
        aplicar(p, request);
        p.setActivo(true);
        return toDTO(productoRepository.save(p));
    }

    @Override
    public ProductDTO actualizar(Long id, ProductRequestDTO request) {
        Producto p = buscar(id);
        aplicar(p, request);
        return toDTO(productoRepository.save(p));
    }

    @Override
    public void eliminar(Long id) {
        productoRepository.delete(buscar(id));
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    private void aplicar(Producto p, ProductRequestDTO r) {
        p.setNombre(r.nombre().trim());
        p.setDescripcion(r.descripcion());
        p.setPrecio(r.precio());
        p.setStock(r.stock());
    }

    private ProductDTO toDTO(Producto p) {
        return new ProductDTO(p.getId(), p.getNombre(), p.getDescripcion(),
                p.getPrecio(), p.getStock(), p.getActivo());
    }
}
