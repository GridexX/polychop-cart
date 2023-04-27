package fr.dopolytech.polyshop.cart.repositories;

import org.springframework.stereotype.Repository;

import fr.dopolytech.polyshop.cart.dtos.AddToCartDto;
import fr.dopolytech.polyshop.cart.models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

@Repository
public class CartRepository {

    @Autowired
    private ReactiveRedisOperations<String, Long> purchaseOperations;

    public Mono<Product> addToCart(AddToCartDto dto) {
        return purchaseOperations.opsForValue().increment(dto.productId, dto.amount)
                .map(quantity -> new Product(dto.productId, quantity.intValue()));
    }

    public Mono<Void> clearProduct(String productId) {
        return purchaseOperations.delete(productId).then();
    }

    public Mono<Product> removeFromCart(AddToCartDto dto) {
        if (!purchaseOperations.hasKey(dto.productId).block()) {
            return Mono.just(new Product(dto.productId, 0));
        }
        if (purchaseOperations.opsForValue().get(dto.productId).block() <= dto.amount) {
            return purchaseOperations.delete(dto.productId).map(count -> new Product(dto.productId, 0));
        }
        return purchaseOperations.opsForValue().decrement(dto.productId, dto.amount)
                .map(quantity -> new Product(dto.productId, quantity.intValue()));
    }

    public Flux<Product> getProducts() {
        return purchaseOperations
                .keys("*")
                .flatMap(key -> purchaseOperations.opsForValue().get(key)
                        .map(quantity -> new Product(key, quantity.intValue())));
    }

    public Mono<Void> clearProducts() {
        return purchaseOperations.delete(purchaseOperations.keys("*")).then();
    }

}
