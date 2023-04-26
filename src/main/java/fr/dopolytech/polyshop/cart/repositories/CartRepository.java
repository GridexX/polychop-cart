package fr.dopolytech.polyshop.cart.repositories;

import org.springframework.stereotype.Repository;

import fr.dopolytech.polyshop.cart.models.Product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Repository
public class CartRepository {

    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Product save(Product product) {
        redisTemplate.opsForHash().put("Product", product.productId, product);
        return product;
    }

    public Product findById(String id) {
        return (Product) redisTemplate.opsForHash().get("Product", id);
    }

    public List<Object> findAll() {
        return redisTemplate.opsForHash().values("Product");
    }

    public void deleteById(String id) {
        redisTemplate.opsForHash().delete("Product", id);
    }

    public void deleteAll() {
        redisTemplate.delete("Product");
    }

}
