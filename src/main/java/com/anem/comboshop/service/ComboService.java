package com.anem.comboshop.service;

import com.anem.comboshop.domain.Product;
import com.anem.comboshop.domain.Purpose;
import com.anem.comboshop.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComboService {
    private final ProductRepository productRepository;
    private final Random random = new Random();

    public ComboService(ProductRepository productRepository){ this.productRepository = productRepository; }

    public ComboResult generate(Purpose purpose, BigDecimal budget, boolean includeSurprise){
        List<Product> available = productRepository.findByPurposeAndStockGreaterThan(purpose, 0);

        BigDecimal maxBudget = (budget == null || budget.compareTo(BigDecimal.ZERO) <= 0)
                ? new BigDecimal("999999")
                : budget;

        List<Product> sorted = available.stream()
                .sorted(Comparator
                        .comparing((Product p) -> ratio(p.getUtilityScore(), p.getPrice())).reversed()
                        .thenComparing(Product::getPrice))
                .collect(Collectors.toList());

        List<Product> picked = new ArrayList<>();
        BigDecimal running = BigDecimal.ZERO;

        for(Product p : sorted){
            if(picked.size() >= 6) break;
            if(running.add(p.getPrice()).compareTo(maxBudget) <= 0){
                picked.add(p);
                running = running.add(p.getPrice());
            }
        }

        boolean surpriseAdded = false;
        if(includeSurprise){
            List<Product> surprises = productRepository.findByStockGreaterThan(0).stream()
                    .filter(Product::isSurpriseEligible)
                    .filter(p -> picked.stream().noneMatch(x -> Objects.equals(x.getId(), p.getId())))
                    .toList();
            if(!surprises.isEmpty()){
                Product surprise = surprises.get(random.nextInt(surprises.size()));
                if(budget == null || budget.compareTo(BigDecimal.ZERO) <= 0 ||
                        running.add(surprise.getPrice()).compareTo(maxBudget) <= 0){
                    picked.add(surprise);
                    running = running.add(surprise.getPrice());
                    surpriseAdded = true;
                }
            }
        }

        return new ComboResult(picked, running, surpriseAdded);
    }

    private double ratio(int utility, BigDecimal price){
        double p = price.doubleValue();
        if(p <= 0.0) return utility;
        return utility / p;
    }

    public record ComboResult(List<Product> products, BigDecimal total, boolean surpriseAdded){}
}
