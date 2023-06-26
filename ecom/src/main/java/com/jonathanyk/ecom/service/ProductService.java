package com.jonathanyk.ecom.service;

import com.jonathanyk.ecom.model.Product;
import com.jonathanyk.ecom.repository.ProductRepository;
import com.jonathanyk.ecom.model.ProductRecord;
import com.jonathanyk.ecom.service.search.SearchAlgorithm;
import com.jonathanyk.ecom.service.search.SearchAlgoName;
import com.jonathanyk.ecom.service.search.SearchFactory;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepo;

    @Autowired
    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public void addProductToDb(ProductRecord request) {
        Product newProd = new Product();
        newProd.setPrice(request.price());
        newProd.setName(request.name());
        productRepo.save(newProd);

    }

    public Optional<Product> retProductById(Integer id) {
        return productRepo.findById(id);
    }

    public boolean doesProductExist(Integer id) {
        Optional<Product> productOptional = retProductById(id);
        return productOptional.isPresent();
    }

    public ResponseEntity<String> deleteProductFromDb(Integer id) {
        if(doesProductExist(id)) {
            productRepo.deleteById(id);
            return ResponseEntity.ok("Product deleted successfully");
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product with id %x not found in DB",id));
    }

    public ResponseEntity<String> updateProductInDb(Integer id, ProductRecord updatedProduct) {
        Optional<Product> existingProduct = retProductById(id);
        if(existingProduct.isPresent()) {
            Product currProd = existingProduct.get();

            // Update the properties of the currProd with the updatedProduct
            currProd.setName(updatedProduct.name());
            currProd.setPrice(updatedProduct.price());

            productRepo.save(currProd);
            return ResponseEntity.ok("Product updated successfully");
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    public ResponseEntity<String> loadProductsCsv() {

        try (CSVReader csvReader = new CSVReader(new FileReader("src/main/java/com/jonathanyk/ecom/model/importProducts.csv"))) {
            List<Product> products = new ArrayList<>();
            String[] record = csvReader.readNext();
            while (record != null) {
                Product product = new Product();
                product.setPrice(Double.parseDouble(record[0]));
                product.setName(record[1]);
                products.add(product);
            }
            productRepo.saveAll(products);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CSV file not found. " + e.getMessage());
        } catch (CsvValidationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CSV file validation exception: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("IO exception occurred: " + e.getMessage());
        }

        return ResponseEntity.ok("Product updated successfully");
    }

    public ResponseEntity<String> binarySearchExec() {
        SearchFactory searchFactory = new SearchFactory();
        SearchAlgorithm binarySearchAlgorithm = searchFactory.getSearch(SearchAlgoName.BINARY_SEARCH);
        return ResponseEntity.ok("TODO: FINISH IMPLEMENTATION!");

    }
}
