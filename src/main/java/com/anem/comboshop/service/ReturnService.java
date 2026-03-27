package com.anem.comboshop.service;

import com.anem.comboshop.domain.*;
import com.anem.comboshop.repo.OrderRepository;
import com.anem.comboshop.repo.ProductRepository;
import com.anem.comboshop.repo.ReturnRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ReturnService {
    private final ReturnRequestRepository returnRepo;
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final FileStorageService fileStorage;
    private final int windowDays;

    public ReturnService(ReturnRequestRepository returnRepo, OrderRepository orderRepo, ProductRepository productRepo,
                         FileStorageService fileStorage,
                         @Value("${app.returns.window-days:7}") int windowDays){
        this.returnRepo = returnRepo;
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.fileStorage = fileStorage;
        this.windowDays = windowDays;
    }

    @Transactional
    public ReturnRequest create(String username, Long orderId, Long productId, int qty, String reason, MultipartFile image) throws IOException {
        CustomerOrder order = orderRepo.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if(!order.getUsername().equals(username)) throw new IllegalStateException("You can request return only for your own orders");
        if(order.getStatus() == OrderStatus.CANCELLED) throw new IllegalStateException("Order is cancelled. Return not allowed.");

        long days = ChronoUnit.DAYS.between(order.getCreatedAt(), LocalDateTime.now());
        if(days > windowDays) throw new IllegalStateException("Return window expired (" + windowDays + " days).");

        int orderedQty = order.getItems().stream()
                .filter(oi -> oi.getProduct().getId().equals(productId))
                .mapToInt(OrderItem::getQuantity).sum();

        if(orderedQty <= 0) throw new IllegalStateException("Product is not part of this order");
        if(qty <= 0 || qty > orderedQty) throw new IllegalStateException("Invalid return quantity");

        Product product = productRepo.findById(productId).orElseThrow();

        ReturnRequest rr = new ReturnRequest();
        rr.setUsername(username);
        rr.setOrder(order);
        rr.setProduct(product);
        rr.setQuantity(qty);
        rr.setReason(reason == null ? "" : reason.trim());
        rr.setStatus(ReturnStatus.REQUESTED);
        rr.setCreatedAt(LocalDateTime.now());
        rr.setImagePath(fileStorage.saveOptional(image));

        return returnRepo.save(rr);
    }

    @Transactional
    public void updateStatus(Long returnId, ReturnStatus status){
        ReturnRequest rr = returnRepo.findById(returnId).orElseThrow(() -> new IllegalArgumentException("Return not found"));
        rr.setStatus(status);

        if(status == ReturnStatus.REFUNDED){
            Product p = rr.getProduct();
            p.setStock(p.getStock() + rr.getQuantity());
            productRepo.save(p);
        }
        returnRepo.save(rr);
    }
}
