package com.anem.comboshop.web;

import com.anem.comboshop.domain.Product;
import com.anem.comboshop.domain.Purpose;
import com.anem.comboshop.domain.ReturnStatus;
import com.anem.comboshop.repo.OrderRepository;
import com.anem.comboshop.repo.ProductRepository;
import com.anem.comboshop.repo.ReturnRequestRepository;
import com.anem.comboshop.service.ReturnService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReturnRequestRepository returnRepository;
    private final ReturnService returnService;

    public AdminController(ProductRepository productRepository, OrderRepository orderRepository, ReturnRequestRepository returnRepository, ReturnService returnService){
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.returnRepository = returnRepository;
        this.returnService = returnService;
    }

    @GetMapping
    public String dashboard(Model model){
        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("orderCount", orderRepository.count());
        model.addAttribute("pendingReturns", returnRepository.findByStatusOrderByCreatedAtDesc(ReturnStatus.REQUESTED));
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String products(@RequestParam(required = false) Long editId, Model model){
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("purposes", Purpose.values());

        Product form = (editId != null) ? productRepository.findById(editId).orElse(new Product()) : new Product();
        model.addAttribute("product", form);
        model.addAttribute("editId", editId);
        return "admin/products";
    }

    @PostMapping("/products/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult br, Model model){
        if(br.hasErrors()){
            model.addAttribute("products", productRepository.findAll());
            model.addAttribute("purposes", Purpose.values());
            return "admin/products";
        }
        productRepository.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id){
        productRepository.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/orders")
    public String orders(Model model){
        model.addAttribute("orders", orderRepository.findAll());
        return "admin/orders";
    }

    @GetMapping("/returns")
    public String returns(Model model){
        model.addAttribute("returns", returnRepository.findAll());
        model.addAttribute("statuses", ReturnStatus.values());
        return "admin/returns";
    }

    @PostMapping("/returns/{id}/status")
    public String updateReturn(@PathVariable Long id, @RequestParam ReturnStatus status){
        returnService.updateStatus(id, status);
        return "redirect:/admin/returns";
    }
}
