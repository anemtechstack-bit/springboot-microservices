package com.anem.comboshop.web;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.anem.comboshop.cart.Cart;
import com.anem.comboshop.domain.CustomerOrder;
import com.anem.comboshop.domain.Purpose;
import com.anem.comboshop.repo.OrderRepository;
import com.anem.comboshop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository){
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    private Cart cart(HttpSession session){
        Cart c = (Cart) session.getAttribute("CART");
        if(c == null){
            c = new Cart();
            session.setAttribute("CART", c);
        }
        return c;
    }

    @GetMapping
    public String list(Authentication auth, Model model){
        model.addAttribute("orders", orderRepository.findByUsernameOrderByCreatedAtDesc(auth.getName()));
        return "orders";
    }

    @PostMapping("/checkout")
    public String checkout(Authentication auth, HttpSession session, Model model){
        Cart c = cart(session);
        Purpose purpose = (Purpose) session.getAttribute("LAST_PURPOSE");
        if(purpose == null) purpose = Purpose.HOSTEL_ESSENTIALS;

        try{
            CustomerOrder order = orderService.checkout(auth.getName(), purpose, c);
            c.clear();
            model.addAttribute("order", order);
            return "checkout_success";
        }catch(Exception e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cart", c);
            return "cart";
        }
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Authentication auth, Model model){
		/*
		 * CustomerOrder order = orderRepository.findById(id).orElseThrow();
		 * if(!order.getUsername().equals(auth.getName())) throw new
		 * IllegalStateException("Not allowed"); model.addAttribute("order", order);
		 * return "order_details";
		 */  
    	var order = orderRepository.findByIdAndUsernameWithItems(id, auth.getName())
    	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    	model.addAttribute("order", order);
    	return "order_details";	
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, Authentication auth){
        orderService.cancelOrder(auth.getName(), id);
        return "redirect:/orders/" + id;
    }
}
