package com.anem.comboshop.web;

import com.anem.comboshop.domain.Purpose;
import com.anem.comboshop.service.ComboService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/combo")
public class ComboController {

    private final ComboService comboService;
    public ComboController(ComboService comboService){ this.comboService = comboService; }

    @GetMapping
    public String form(Model model){
        model.addAttribute("purposes", Purpose.values());
        return "combo";
    }

    @PostMapping
    public String generate(@RequestParam Purpose purpose,
                           @RequestParam(required = false) BigDecimal budget,
                           @RequestParam(defaultValue = "false") boolean surprise,
                           Model model, HttpSession session){
        var result = comboService.generate(purpose, budget, surprise);
        String idsCsv = result.products().stream().map(p -> String.valueOf(p.getId())).collect(Collectors.joining(","));
        session.setAttribute("LAST_PURPOSE", purpose);

        model.addAttribute("purposes", Purpose.values());
        model.addAttribute("selectedPurpose", purpose);
        model.addAttribute("budget", budget);
        model.addAttribute("surprise", surprise);
        model.addAttribute("combo", result);
        model.addAttribute("idsCsv", idsCsv);
        return "combo";
    }
}
