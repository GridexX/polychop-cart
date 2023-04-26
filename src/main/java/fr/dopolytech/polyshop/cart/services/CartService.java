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

import fr.dopolytech.polyshop.cart.messages.ErrorMessage;
import fr.dopolytech.polyshop.cart.messages.ShoppingCartMessage;
import fr.dopolytech.polyshop.cart.models.Product;
import fr.dopolytech.polyshop.cart.repositories.CartRepository;

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
  //   this.rabbitTemplate = rabbitTemplate;
  //   this.orderQueue = orderQueue;
  // }

  public Product addToCart(String id) {
    Product product = getProductById(id);
    if (product != null) {
      product.amount += 1;
    } else {
      product = new Product(Long.parseLong(id), 1);
    }
    return cartRepository.save(product);
  }

  public List<Product> findAll() {
    List<Object> objects = cartRepository.findAll();
    return objects.stream().map(object -> (Product) object).collect(Collectors.toList());
  }

  public Product save(Product product) {
    return cartRepository.save(product);
  }

  public Product getProductById(String id) {
    return cartRepository.findById(id);
}


  public void removeProductFrom(String id) {
    Product shoppingCartProduct = getProductById(id);

    boolean deleteProduct = false;
    if (shoppingCartProduct != null) {
      if(shoppingCartProduct.amount == 1) {
        deleteProduct = true;
      } else {
        shoppingCartProduct.amount -= 1;
      }
      cartRepository.save(shoppingCartProduct);
    } else {
      logger.error("Product not found in cart");
      deleteProduct = true;
    }

    if(deleteProduct) { 
      cartRepository.deleteById(id);
    }
    
  }

  public void clear() {
    cartRepository.deleteAll();
  }

  // -- RABBITMQ --
  // Those methods are used to communicate with the order service

  // This method is called when a message is received from the order service to
  public void checkout() {
    List<Product> products = findAll();
    ShoppingCartMessage shoppingMessage = new ShoppingCartMessage(products);
    try {
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(shoppingMessage);
      rabbitTemplate.convertAndSend(orderQueue.getName(), json);
    } catch (Exception e) {
      logger.error("Failed to convert object", e);
    }
    // Clear the cart after checkout
    clear();
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
          save(shoppingCartProduct);
        }
      );

    } catch (Exception e) {
      logger.error("Failed to parse JSON payload : ", e);
    }
  }

}
