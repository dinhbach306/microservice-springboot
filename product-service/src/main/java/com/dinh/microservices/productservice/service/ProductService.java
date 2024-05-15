package com.dinh.microservices.productservice.service;

import com.dinh.microservices.productservice.dto.ProductRequest;
import com.dinh.microservices.productservice.dto.ProductResponse;
import com.dinh.microservices.productservice.model.Product;
import com.dinh.microservices.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
	private static final Logger log = LoggerFactory.getLogger(ProductService.class);
	private final ProductRepository productRepository;

	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder()
				.name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.build();

		productRepository.save(product);
		log.info("Product created successfully. Product: {}", product.getId());
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();
		log.info("Retrieved all products. Total products: {}", products.size());
		return products.stream()
				.map(this::mapToProductResponse)
				.collect(Collectors.toList());
	}

	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	}
}
