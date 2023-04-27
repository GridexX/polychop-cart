package fr.dopolytech.polyshop.cart.dtos;

public class AddToCartDto {
    public String productId;
    public long amount;

    public String getProductId() {
        return productId;
    }

    public long getQuantity() {
        return amount ;
    }

    public AddToCartDto(String productId, long amount ) {
        this.productId = productId;
        this.amount = amount;
    }
}
