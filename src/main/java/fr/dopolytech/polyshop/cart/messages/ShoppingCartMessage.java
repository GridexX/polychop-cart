package fr.dopolytech.polyshop.cart.messages;

import java.util.List;

import fr.dopolytech.polyshop.cart.models.Product;

public class ShoppingCartMessage {
  public List<ProductItem> products;
  
  public ShoppingCartMessage() {
  }

  // public ShoppingCartMessage(List<ProductItem> products) {
  //   this.products = products;
  // }

  public ShoppingCartMessage(List<Product> products) {
    for (Product purchase : products) {
      this.products.add(new ProductItem(purchase));
    }
  }
}