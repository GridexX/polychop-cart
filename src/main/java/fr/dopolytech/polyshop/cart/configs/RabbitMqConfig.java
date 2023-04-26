package fr.dopolytech.polyshop.cart.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.dopolytech.polyshop.cart.services.CartService;

@Configuration
public class RabbitMqConfig {
  
  // Queue to send messages to the order service
  @Bean
  public Queue orderQueue() {
    return new Queue("order", false);
  }

  // Queue to listen for errors messages from the order service
  @Bean
  public Queue orderCancelQueue() {
    return new Queue("order_cancel", false);
  }

  @Bean 
  public MessageListenerAdapter listenerAdapterCancel(CartService cartService) {
    return new MessageListenerAdapter(cartService, "receiveMessageCancel");
  }

  @Bean
  public SimpleMessageListenerContainer containerCancel(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapterCancel) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames("order_cancel");
    container.setMessageListener(listenerAdapterCancel);
    return container;
  }


}
