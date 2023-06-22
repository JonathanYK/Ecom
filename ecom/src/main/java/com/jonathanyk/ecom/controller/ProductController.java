package com.jonathanyk.ecom.controller;

import com.jonathanyk.ecom.model.ProductRecord;
import com.jonathanyk.ecom.model.Product;
import com.jonathanyk.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productServ;

    @Autowired
    public ProductController(ProductService productServ) {
        this.productServ = productServ;
    }

    @GetMapping("getAll")
    public List<Product> AllProducts() {
        return productServ.getAllProducts();
    }

    @PostMapping("addProduct")
    public ResponseEntity<String> addProduct(@RequestBody ProductRecord request) {
        productServ.addProductToDb(request);
        return ResponseEntity.ok("Product added successfully");
    }

    @PutMapping("updateProduct/{p_id}")
    public ResponseEntity<String> updateProductName(@PathVariable("p_id") Integer id, @RequestBody ProductRecord updatedProduct) {
        return productServ.updateProductInDb(id, updatedProduct);
    }

    @DeleteMapping("deleteProduct/{p_id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("p_id") Integer id) {
        return productServ.deleteProductFromDb(id);
    }


    @GetMapping("loadProductsFromCsv")
    public ResponseEntity<String> loadProductsCsv() {
        return productServ.loadProductsCsv();
    }
}
