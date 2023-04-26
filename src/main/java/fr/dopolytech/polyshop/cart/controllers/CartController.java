package fr.dopolytech.polyshop.cart.controllers;

import java.util.List;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import fr.dopolytech.polyshop.cart.dtos.AddToCartDto;
import fr.dopolytech.polyshop.cart.models.Product;
import fr.dopolytech.polyshop.cart.models.Purchase;
import fr.dopolytech.polyshop.cart.services.CartService;
import jakarta.inject.Inject;

@RestController
@RequestMapping("/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping(value = "/{id}")
	public Product addToCart(@PathVariable("id") String id) {
		return cartService.addToCart(id);
	}

	@DeleteMapping(value = "/{id}")
	public void removeFromCart(@PathVariable("id") String id) {
		cartService.removeProductFrom(id);
	}

	@GetMapping
	public List<Product> findAll() {
		return cartService.findAll();
	}

	@DeleteMapping("/clear")
	public void clear() {
		cartService.clear();
	}

	@PostMapping("/checkout")
	public void checkout() {
		cartService.checkout();
	}
}