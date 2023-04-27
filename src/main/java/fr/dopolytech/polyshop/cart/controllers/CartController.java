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
import org.springframework.web.bind.annotation.ResponseStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import fr.dopolytech.polyshop.cart.dtos.AddToCartDto;
import fr.dopolytech.polyshop.cart.dtos.ExceptionDto;
import fr.dopolytech.polyshop.cart.exceptions.CartEmptyException;
import fr.dopolytech.polyshop.cart.models.Product;
import fr.dopolytech.polyshop.cart.models.Purchase;
import fr.dopolytech.polyshop.cart.services.CartService;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("/product/{id}")
	public Mono<Product> addToCart(@PathVariable("id") String id) {
		return cartService.addToCart(new AddToCartDto(id, 1));
	}

	@PostMapping("/product")
	public Mono<Product> addToCart(@RequestBody AddToCartDto dto) {
		return cartService.addToCart(dto);
	}

	@DeleteMapping("/product/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> removeFromCart(@PathVariable("id") String id) {
		return cartService.clearProduct(id);
	}

	@GetMapping
	public Flux<Product> findAllProduct() {
		return cartService.findAll();
	}

	@GetMapping("/product")
	public Flux<Product> findAll() {
		return cartService.findAll();
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> clear() {
		return cartService.clear();
	}

	@PostMapping("/checkout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<ResponseEntity<Object>> checkout() {
		try {
			// Launch the checkout process and return a Mono with a ResponseEntity
			return cartService.checkout().map(purchase -> {
				// If the checkout process is successful, return a ResponseEntity with the
				// purchase
				return ResponseEntity.noContent().build();
			});
		} catch (CartEmptyException e) {
			return Mono.just(
					ResponseEntity.badRequest().body(ExceptionDto.create(HttpStatus.BAD_REQUEST.toString(), e.getMessage())));
		} catch (Exception e) {
			return Mono.just(ResponseEntity.internalServerError()
					.body(ExceptionDto.create(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage())));
		}
	}
}