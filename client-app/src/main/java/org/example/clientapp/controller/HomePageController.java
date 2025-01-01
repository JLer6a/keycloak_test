package org.example.clientapp.controller;


import lombok.RequiredArgsConstructor;
import org.example.clientapp.client.ProductsRestClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(("/"))
@RequiredArgsConstructor
public class HomePageController {

    private final ProductsRestClient productsRestClient;

    @GetMapping()
    public String getProductsList() {
        return "catalogue/products/index";
    }
}
