package com.anem.comboshop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(length = 2000)
    private String description;

    @NotNull @DecimalMin("0.00")
    private BigDecimal price;

    @NotNull @Min(0)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Purpose purpose;

    @NotNull @Min(1) @Max(10)
    private Integer utilityScore;

    private boolean surpriseEligible;

    // For UI demo (you can host images anywhere)
    private String imageUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Purpose getPurpose() { return purpose; }
    public void setPurpose(Purpose purpose) { this.purpose = purpose; }
    public Integer getUtilityScore() { return utilityScore; }
    public void setUtilityScore(Integer utilityScore) { this.utilityScore = utilityScore; }
    public boolean isSurpriseEligible() { return surpriseEligible; }
    public void setSurpriseEligible(boolean surpriseEligible) { this.surpriseEligible = surpriseEligible; }
    public String getImageUrl(){ return imageUrl; }
    public void setImageUrl(String imageUrl){ this.imageUrl = imageUrl; }
}
