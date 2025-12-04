package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService 
{
	@Autowired
	private ProductRepository productRepository;

	public Product createProduct(Product product) {
		Product saved = productRepository.save(product);
		log.info("Saved product to MongoDB: {}", saved);
		return saved;
	}

	@Cacheable(value="products",key="#id")
	public Product getProductById(String id) 
	{
		log.info("Fetching product id: {}", id); 
		return productRepository.findById(id).orElse(null);
	}
	
	public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
	
	@CachePut(value = "products", key = "#id")
	public Product updateProduct(String id, Product product) {
		return productRepository.findById(id).map(existing -> {
			existing.setName(product.getName());
			existing.setPrice(product.getPrice());
			Product updated = productRepository.save(existing);
			log.info("product and cache updated id: {}", updated);
			return updated;
		}).orElse(null);
    }
	@CacheEvict(value="products", key="#id")
    public void deleteProduct(String id) 
	{
        productRepository.deleteById(id);
        log.info("Deleted product from mongodb and cache removed: id={}", id);
    }
}
