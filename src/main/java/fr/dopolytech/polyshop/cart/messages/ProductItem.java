package fr.dopolytech.polyshop.cart.messages;

import fr.dopolytech.polyshop.cart.models.Product;

public class ProductItem {
  public String productId;
  public long amount;

  public ProductItem() {
  }

  public ProductItem(Product product) {
    this.productId = product.productId;
    this.amount = product.amount;
  }
}