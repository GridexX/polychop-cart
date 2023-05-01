package fr.dopolytech.polyshop.cart.messages;

import fr.dopolytech.polyshop.cart.models.Product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductItem {
  public String productId;
  public long amount;

  public ProductItem() {
  }

  public ProductItem(Product product) {
    this.productId = product.id;
    this.amount = product.amount;
  }
}