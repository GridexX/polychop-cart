package fr.dopolytech.polyshop.cart.dtos;

import lombok.Data;

@Data
public class AddToCartDto {
    public String id;
    public long amount;

    public AddToCartDto(String id, long amount ) {
        this.id = id;
        this.amount = amount;
    }
}
