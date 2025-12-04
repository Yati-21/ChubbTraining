package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping
	public Product create(@RequestBody Product product) {
		return productService.createProduct(product);
	}

	@GetMapping("/{id}")
	public com.example.demo.entity.Product getById(@PathVariable String id) {
		return productService.getProductById(id);
	}

	@GetMapping
	public List<Product> getAll() {
		return productService.getAllProducts();
	}

	@PutMapping("/{id}")
	public Product update(@PathVariable String id, @RequestBody Product product) {
		return productService.updateProduct(id, product);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		productService.deleteProduct(id);
	}
}