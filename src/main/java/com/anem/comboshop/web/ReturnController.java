/*
 * package com.anem.comboshop.web;
 * 
 * import com.anem.comboshop.repo.OrderRepository; import
 * com.anem.comboshop.repo.ReturnRequestRepository; import
 * com.anem.comboshop.service.ReturnService; import
 * jakarta.validation.constraints.NotBlank; import
 * org.springframework.security.core.Authentication; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.*; import
 * org.springframework.web.multipart.MultipartFile;
 * 
 * @Controller
 * 
 * @RequestMapping("/returns") public class ReturnController {
 * 
 * private final OrderRepository orderRepository; private final
 * ReturnRequestRepository returnRepo; private final ReturnService
 * returnService;
 * 
 * public ReturnController(OrderRepository orderRepository,
 * ReturnRequestRepository returnRepo, ReturnService returnService){
 * this.orderRepository = orderRepository; this.returnRepo = returnRepo;
 * this.returnService = returnService; }
 * 
 * @GetMapping public String list(Authentication auth, Model model){ var orders
 * = orderRepository.findByUsernameOrderByCreatedAtDesc(auth.getName());
 * model.addAttribute("returns",
 * returnRepo.findByUsernameOrderByCreatedAtDesc(auth.getName()));
 * model.addAttribute("orders", orders);
 * 
 * try{ var mapper = new com.fasterxml.jackson.databind.ObjectMapper(); var
 * payload = orders.stream().map(o -> java.util.Map.of( "id", o.getId(),
 * "items", o.getItems().stream().map(i -> java.util.Map.of( "productId",
 * i.getProduct().getId(), "productName", i.getProduct().getName(), "qty",
 * i.getQuantity() )).toList() )).toList(); model.addAttribute("ordersJson",
 * mapper.writeValueAsString(payload)); }catch(Exception e){
 * model.addAttribute("ordersJson", "[]"); }
 * 
 * return "returns"; }
 * 
 * 
 * @PostMapping("/request") public String request(Authentication auth,
 * 
 * @RequestParam Long orderId,
 * 
 * @RequestParam Long productId,
 * 
 * @RequestParam int qty,
 * 
 * @RequestParam @NotBlank String reason,
 * 
 * @RequestParam(required = false) MultipartFile image, Model model){ try{
 * returnService.create(auth.getName(), orderId, productId, qty, reason, image);
 * return "redirect:/returns?ok=1"; }catch(Exception e){
 * model.addAttribute("error", e.getMessage()); model.addAttribute("returns",
 * returnRepo.findByUsernameOrderByCreatedAtDesc(auth.getName()));
 * model.addAttribute("orders",
 * orderRepository.findByUsernameOrderByCreatedAtDesc(auth.getName())); return
 * "returns"; } } }
 */


package com.anem.comboshop.web;

import com.anem.comboshop.repo.OrderRepository;
import com.anem.comboshop.repo.ReturnRequestRepository;
import com.anem.comboshop.service.ReturnService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("/returns")
public class ReturnController {

    private final OrderRepository orderRepository;
    private final ReturnRequestRepository returnRepo;
    private final ReturnService returnService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReturnController(OrderRepository orderRepository,
                            ReturnRequestRepository returnRepo,
                            ReturnService returnService) {
        this.orderRepository = orderRepository;
        this.returnRepo = returnRepo;
        this.returnService = returnService;
    }

    @GetMapping
    public String list(Authentication auth, Model model) {
        loadReturnPageData(auth.getName(), model);
        return "returns";
    }

    @PostMapping("/request")
    public String request(Authentication auth,
                          @RequestParam Long orderId,
                          @RequestParam Long productId,
                          @RequestParam int qty,
                          @RequestParam @NotBlank String reason,
                          @RequestParam(required = false) MultipartFile image,
                          Model model) {
        try {
            returnService.create(auth.getName(), orderId, productId, qty, reason, image);
            return "redirect:/returns?ok=1";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            loadReturnPageData(auth.getName(), model);
            return "returns";
        }
    }

    private void loadReturnPageData(String username, Model model) {
        var orders = orderRepository.findByUsernameWithItems(username);

        model.addAttribute("returns", returnRepo.findByUsernameOrderByCreatedAtDesc(username));
        model.addAttribute("orders", orders);

        try {
            var payload = orders.stream()
                    .map(o -> Map.of(
                            "id", o.getId(),
                            "items", o.getItems().stream()
                                    .map(i -> Map.of(
                                            "productId", i.getProduct().getId(),
                                            "productName", i.getProduct().getName(),
                                            "qty", i.getQuantity()
                                    ))
                                    .toList()
                    ))
                    .toList();

            model.addAttribute("ordersJson", objectMapper.writeValueAsString(payload));
        } catch (Exception e) {
            model.addAttribute("ordersJson", "[]");
        }
    }
}