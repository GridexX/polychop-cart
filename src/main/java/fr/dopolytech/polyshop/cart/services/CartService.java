package fr.dopolytech.polyshop.cart.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dopolytech.polyshop.cart.dtos.AddToCartDto;
import fr.dopolytech.polyshop.cart.exceptions.CartEmptyException;
import fr.dopolytech.polyshop.cart.exceptions.ClearProductsException;
import fr.dopolytech.polyshop.cart.messages.ErrorMessage;
import fr.dopolytech.polyshop.cart.messages.ShoppingCartMessage;
import fr.dopolytech.polyshop.cart.models.Product;
import fr.dopolytech.polyshop.cart.repositories.CartRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CartService {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private Queue orderQueue;

  @Autowired
  private CartRepository cartRepository;

  private static final Logger logger = LoggerFactory.getLogger(CartService.class);

  // @Autowired
  // public CartService(RabbitTemplate rabbitTemplate, Queue orderQueue) {
  // this.rabbitTemplate = rabbitTemplate;
  // this.orderQueue = orderQueue;
  // }

  public Mono<Product> addToCart(AddToCartDto dto) {
    return cartRepository.addToCart(dto);
  }

  public Flux<Product> findAll() {
    return cartRepository.getProducts();
  }

  public Mono<Product> removeProductFrom(AddToCartDto dto) {
    return cartRepository.removeFromCart(dto);
  }

  public Mono<Void> clearProduct(String productId) {
    return cartRepository.clearProduct(productId);
  }

  public Mono<Void> clear() throws ClearProductsException {
    return cartRepository.clearProducts()
        .onErrorMap(error -> new ClearProductsException("Failed to clear products", error));

  }

  // -- RABBITMQ --
  // Those methods are used to communicate with the order service

  // This method is called when a message is received from the order service to
  public Mono<Void> checkout() throws Exception {
    List<Product> products = findAll().collectList().block();

    if (products.isEmpty()) {
      logger.error("Cart is empty");
      throw new CartEmptyException();
    }

    ShoppingCartMessage shoppingMessage = new ShoppingCartMessage(products);
    logger.info("Sending message to order service : " + shoppingMessage);
    try {
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(shoppingMessage);
      rabbitTemplate.convertAndSend(orderQueue.getName(), json);
    } catch (Exception e) {
      logger.error("Failed to convert object", e);
      throw e;
    }
    // Clear the cart after checkout
    return clear().flatMap(cleared -> Mono.empty());
  }

  // This method is called when a message is received from the order service to
  // cancel an order
  public void receiveMessageCancel(String message) {

    // It should retrieve all product that were in the order and add them back to
    // the cart
    ObjectMapper mapper = new ObjectMapper();
    try {
      ErrorMessage errorMessage = mapper.readValue(message, ErrorMessage.class);
      logger.info("Received error message from order service : " + errorMessage);

      errorMessage.products.forEach(
          product -> {
            Product shoppingCartProduct = new Product(product.productId, product.amount);
            cartRepository.addToCart(new AddToCartDto(
                shoppingCartProduct.productId,
                shoppingCartProduct.amount));
          });

    } catch (Exception e) {
      logger.error("Failed to parse JSON payload : ", e);
    }
  }

}
