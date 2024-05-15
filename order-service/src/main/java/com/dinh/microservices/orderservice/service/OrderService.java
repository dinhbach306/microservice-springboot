package com.dinh.microservices.orderservice.service;

import com.dinh.microservices.orderservice.dto.InventoryResponse;
import com.dinh.microservices.orderservice.dto.OrderLineItemsDto;
import com.dinh.microservices.orderservice.dto.OrderRequest;
import com.dinh.microservices.orderservice.event.OrderPlacedEvent;
import com.dinh.microservices.orderservice.model.Order;
import com.dinh.microservices.orderservice.model.OrderLineItems;
import com.dinh.microservices.orderservice.repository.IOderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	private final IOderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

	public String placeOrder(OrderRequest request) {
		Order order = new Order();
		List<OrderLineItems> list = request.getOrderLineItemsList()
				.stream()
				.map(lineItem -> this.mapToDto(lineItem, order)).toList();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderLineItemsList(list);
		List<String> skuCodes = order.getOrderLineItemsList().stream()
				.map(OrderLineItems::getSkuCode)
				.toList();

		// Call inventory service, and place order if product is in
		// Stock
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory",
						uriBuilder ->  uriBuilder.queryParam("skuCode", skuCodes).build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();

		boolean result = Arrays.stream(inventoryResponseArray)
				.allMatch(InventoryResponse::isInStock);
		if (result) {
			orderRepository.save(order);
			kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
			return "Order placed successfully";
		} else {
			throw new IllegalArgumentException("Product is out of stock");
		}
	}

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto, Order order) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setOrder(order);
		return orderLineItems;
	}
}
