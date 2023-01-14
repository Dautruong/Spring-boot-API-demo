package com.java.apidemo.controllers;

import com.java.apidemo.models.Product;
import com.java.apidemo.models.ResponseObject;
import com.java.apidemo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/Products")
public class ProductController {
    @Autowired
    private ProductRepository repository;
    @GetMapping("")
    List<Product> getAllProducts(){
        /*return List.of(
                new Product(1L,"iphone 11", 2020,2400.0, ""),
                new Product(2L,"iphone 12", 2022,12000.0, "")
        );*/
        return repository.findAll();
    }

    // Get detail product
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findByProduct(@PathVariable Long id){
        Optional<Product> foundProduct = repository.findById(id);
            return  foundProduct.isPresent() ?
                 ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query product successfully", foundProduct)
                 ):
                 ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            new ResponseObject("fail", "Cannot find product with id = " + id, "")
                 );
    }
    // insert new product with Post method
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct){
        // Check 2 products must not have the same name!
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size()>0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert product successfully", repository.save(newProduct))
        );
    }

    //update, insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable long id){
        Product updateProduct = repository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setYear(newProduct.getYear());
                    product.setPrice(newProduct.getPrice());
                    product.setUrl(newProduct.getUrl());
                    return repository.save(product);
                }).orElseGet(() ->{
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update product successfully",updateProduct)
        );
    }

    //Delete a Product
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
        boolean exists = repository.existsById(id);
        if (exists){
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","Delete Product successfully id =" + id, "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed","Cannot find product to delete","")
        );
    }
}
