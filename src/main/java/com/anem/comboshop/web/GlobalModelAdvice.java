package com.anem.comboshop.web;

import com.anem.comboshop.cart.Cart;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session){
        Cart cart = (Cart) session.getAttribute("CART");
        return cart == null ? 0 : cart.countItems();
    }
}
