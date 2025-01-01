package org.example.clientapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.clientapp.client.BadRequestException;
import org.example.clientapp.client.ProductsRestClient;
import org.example.clientapp.controller.payload.NewProductPayload;
import org.example.clientapp.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {

    private final ProductsRestClient productsRestClient;

    @GetMapping("list")
    public String getProductsList(Model model, @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("products", this.productsRestClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
//        this.productsRestClient.registerUser(UserRequestDto.builder()
//                .username("test4")
//                .firstName("Mi-test4")
//                .lastName("Bu-test4")
//                .email("test4@mail.ru")
//                .enabled(true)
//                .emailVerified(true)
//                .credentials(List.of(
//                        UserRequestDto.Credential.builder()
//                                .type("password")
//                                .value("444444")
//                                .isTemporary(false)
//                                .build()
//                ))
//                .build());
        return "catalogue/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("create")
    public String createProduct(NewProductPayload payload,
                                Model model) {
        try {
            Product product = this.productsRestClient.createProduct(payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/new_product";
        }
    }
}
