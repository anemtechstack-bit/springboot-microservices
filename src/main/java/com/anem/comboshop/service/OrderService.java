package com.anem.comboshop.service;

import com.anem.comboshop.cart.Cart;
import com.anem.comboshop.cart.CartItem;
import com.anem.comboshop.domain.*;
import com.anem.comboshop.repo.OrderRepository;
import com.anem.comboshop.repo.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CustomerOrder checkout(String username, Purpose purpose, Cart cart){
        if(cart.getItems().isEmpty()) throw new IllegalStateException("Cart is empty");

        CustomerOrder order = new CustomerOrder();
        order.setUsername(username);
        order.setCreatedAt(LocalDateTime.now());
        order.setPurpose(purpose);
        order.setStatus(OrderStatus.PLACED);

        BigDecimal total = BigDecimal.ZERO;

        for(CartItem ci : cart.getItems()){
            Product p = productRepository.findById(ci.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + ci.getProductId()));
            if(p.getStock() < ci.getQuantity()){
                throw new IllegalStateException("Not enough stock for " + p.getName());
            }
            p.setStock(p.getStock() - ci.getQuantity());

            OrderItem item = new OrderItem();
            item.setProduct(p);
            item.setQuantity(ci.getQuantity());
            item.setUnitPrice(p.getPrice());
            order.addItem(item);

            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        order.setTotalAmount(total);
        productRepository.flush();
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(String username, Long orderId){
        CustomerOrder o = orderRepository.findById(orderId).orElseThrow();
        if(!o.getUsername().equals(username)) throw new IllegalStateException("Not allowed");
        if(o.getStatus() == OrderStatus.CANCELLED) return;

        // restore stock
        for(OrderItem it : o.getItems()){
            Product p = it.getProduct();
            p.setStock(p.getStock() + it.getQuantity());
            productRepository.save(p);
        }
        o.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(o);
    }
}
