package fr.dopolytech.polyshop.cart.models;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Product {
  public String id;
  public long amount;

  public Product() {
  }

  public Product(String id, long amount) {
    this.id = id;
    this.amount = amount;
  }
}
