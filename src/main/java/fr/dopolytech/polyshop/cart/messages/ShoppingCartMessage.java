package fr.dopolytech.polyshop.cart.messages;

import java.util.List;
import java.util.ArrayList;

import fr.dopolytech.polyshop.cart.models.Product;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ShoppingCartMessage {
  public List<ProductItem> products;

  public ShoppingCartMessage() {
  }

  public ShoppingCartMessage(List<Product> products) {
    this.products = new ArrayList<>();
    for (Product purchase : products) {
      this.products.add(new ProductItem(purchase));
    }
  }
  
}