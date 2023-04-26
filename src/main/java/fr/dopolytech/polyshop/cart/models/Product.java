package fr.dopolytech.polyshop.cart.models;

public class Product {
  public long productId;
  public long amount;

  public Product() {
  }

  public Product(long productId, long amount) {
    this.productId = productId;
    this.amount = amount;
  }
}
