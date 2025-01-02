package org.example.backendservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backendservice.controller.payload.NewProductPayload;
import org.example.backendservice.entity.Product;
import org.example.backendservice.service.ProductService;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsRestController {

    private final ProductService productService;

    @GetMapping
    public Iterable<Product> findProducts(
            @RequestParam(name = "filter", required = false) String filter,
            Principal principal) {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) principal;
        Jwt jwt = jwtToken.getToken();
        String username = jwt.getClaimAsString("name");
        return this.productService.findProductsByUserAndTitle(username, filter);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder,
                                           Principal principal)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) principal;
            Jwt jwt = jwtToken.getToken();
            String name = jwt.getClaimAsString("name");

            Product product = this.productService.createProduct(payload.title(), payload.details(), name);
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("/catalogue-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }
    }
}
