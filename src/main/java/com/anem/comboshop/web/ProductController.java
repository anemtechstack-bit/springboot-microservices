package com.anem.comboshop.web;

import com.anem.comboshop.domain.Purpose;
import com.anem.comboshop.repo.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Purpose purpose,
                       @RequestParam(required = false) String q,
                       Model model){
        if(q != null && !q.isBlank()){
            model.addAttribute("products", productRepository.findByNameContainingIgnoreCase(q.trim()));
        } else if(purpose != null){
            model.addAttribute("products", productRepository.findByPurpose(purpose));
        } else {
            model.addAttribute("products", productRepository.findAll());
        }
        model.addAttribute("purposes", Purpose.values());
        model.addAttribute("selectedPurpose", purpose);
        model.addAttribute("q", q);
        return "products";
    }
}
