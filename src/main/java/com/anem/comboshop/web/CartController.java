package com.anem.comboshop.web;

import com.anem.comboshop.cart.Cart;
import com.anem.comboshop.domain.Product;
import com.anem.comboshop.repo.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductRepository productRepository;
    public CartController(ProductRepository productRepository){ this.productRepository = productRepository; }

    private Cart cart(HttpSession session){
        Cart c = (Cart) session.getAttribute("CART");
        if(c == null){
            c = new Cart();
            session.setAttribute("CART", c);
        }
        return c;
    }

    @GetMapping
    public String view(HttpSession session, Model model){
        model.addAttribute("cart", cart(session));
        return "cart";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long productId, @RequestParam(defaultValue="1") int qty, HttpSession session){
        Product p = productRepository.findById(productId).orElseThrow();
        cart(session).add(p.getId(), p.getName(), p.getPrice(), qty);
        return "redirect:/cart";
    }

    @PostMapping("/add-combo")
    public String addCombo(@RequestParam("productIds") String productIds, HttpSession session){
        Cart c = cart(session);
        for(String idStr : productIds.split(",")){
            if(idStr == null || idStr.isBlank()) continue;
            Long id = Long.valueOf(idStr.trim());
            Product p = productRepository.findById(id).orElseThrow();
            c.add(p.getId(), p.getName(), p.getPrice(), 1);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long productId, @RequestParam int qty, HttpSession session){
        cart(session).setQuantity(productId, qty);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long productId, HttpSession session){
        cart(session).remove(productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clear(HttpSession session){
        cart(session).clear();
        return "redirect:/cart";
    }
}
