package com.dinh.microservices.orderservice.controller;

import com.dinh.microservices.orderservice.dto.OrderRequest;
import com.dinh.microservices.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class orderController {

	private final OrderService orderService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
//	@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
//	@TimeLimiter(name = "inventory")
//	@Retry(name = "inventory")
	public String placeOrder(@RequestBody OrderRequest orderRequest) {
		return orderService.placeOrder(orderRequest);
	}

	public String fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
		return "Order service is down. Please try again later.";
	}
}
