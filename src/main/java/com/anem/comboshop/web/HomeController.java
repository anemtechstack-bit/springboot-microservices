package com.anem.comboshop.web;

import com.anem.comboshop.cart.Cart;
import com.anem.comboshop.domain.Purpose;
import com.anem.comboshop.repo.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductRepository productRepository;

    public HomeController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model){
        Cart cart = (Cart) session.getAttribute("CART");        model.addAttribute("purposes", Purpose.values());
        model.addAttribute("products", productRepository.findAll());
        return "index";
    }
}
