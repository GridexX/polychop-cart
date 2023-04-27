package fr.dopolytech.polyshop.cart.models;

public class Product {
  public String productId;
  public long amount;

  public Product() {
  }

  public Product(String productId, long amount) {
    this.productId = productId;
    this.amount = amount;
  }
}
