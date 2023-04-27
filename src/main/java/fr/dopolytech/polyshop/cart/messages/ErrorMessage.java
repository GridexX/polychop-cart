package fr.dopolytech.polyshop.cart.messages;

import fr.dopolytech.polyshop.cart.messages.ProductItem;

import java.util.List;

public class ErrorMessage {
  String errorStatus;
  String message;
  String source;
  public long orderId;
  public List<ProductItem> products;

  public ErrorMessage(String errorStatus, String message, long orderId, List<ProductItem> products) {
    this.errorStatus = errorStatus;
    this.message = message;
    this.source = "cart";
    this.orderId = orderId;
    this.products = products;
    
  }
}
