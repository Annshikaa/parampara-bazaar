package com.parampara.bazaar.product;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> all() {
        return productService.listAll();
    }

    @GetMapping("/{id}")
    public Product byId(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping("/category/{category}")
    public List<Product> byCategory(@PathVariable String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    @GetMapping("/market/{marketOrPlace}")
    public List<Product> byMarket(@PathVariable String marketOrPlace) {
        return productRepository.findByPlaceOrMarketIgnoreCase(marketOrPlace);
    }

    @PreAuthorize("hasRole('VENDOR')")
    @PostMapping
    public Product add(@Valid @RequestBody ProductCreateRequest req) {
        return productService.addProduct(req);
    }
}


