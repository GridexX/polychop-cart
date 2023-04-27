package fr.dopolytech.polyshop.cart.exceptions;

public class CartEmptyException extends Exception {
  public CartEmptyException() {
    super("Cart is empty");
  }
}
