package com.anem.comboshop.config;

import com.anem.comboshop.domain.Product;
import com.anem.comboshop.domain.Purpose;
import com.anem.comboshop.repo.ProductRepository;
import com.anem.comboshop.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserService userService;

    public DataInitializer(ProductRepository productRepository, UserService userService){
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args){
        userService.ensureAdminSeed();

        if(productRepository.count() > 0) return;

        List<Product> seed = List.of(
                p("Bedsheet Set", "Soft bedsheet + pillow cover.", "599", 25, Purpose.HOSTEL_ESSENTIALS, 8, false, "https://images.unsplash.com/photo-1600566753376-12c8ab7fb75b?w=1200"),
                p("Laundry Bag", "Easy carry laundry bag.", "199", 40, Purpose.HOSTEL_ESSENTIALS, 6, true, "https://images.unsplash.com/photo-1520975958225-22519a2b0717?w=1200"),
                p("Steel Water Bottle", "1L durable bottle.", "299", 30, Purpose.HOSTEL_ESSENTIALS, 7, true, "https://images.unsplash.com/photo-1526401485004-2aa7b7df8f54?w=1200"),
                p("Toiletry Kit", "Soap box + pouch + comb.", "249", 50, Purpose.HOSTEL_ESSENTIALS, 7, false, "https://images.unsplash.com/photo-1618477462146-40aeb159d3b6?w=1200"),
                p("Extension Board", "4 socket power strip.", "499", 20, Purpose.HOSTEL_ESSENTIALS, 9, false, "https://images.unsplash.com/photo-1583484969498-36e9b00dbf6f?w=1200"),

                p("Gym Gloves", "Comfort grip gloves.", "349", 35, Purpose.GYM_PACK, 7, false, "https://images.unsplash.com/photo-1517832606294-7e0c0f8ad845?w=1200"),
                p("Shaker Bottle", "Protein shaker 700ml.", "299", 30, Purpose.GYM_PACK, 7, true, "https://images.unsplash.com/photo-1620799139834-6b8f844fbe61?w=1200"),
                p("Resistance Band", "Strength training band.", "399", 25, Purpose.GYM_PACK, 8, false, "https://images.unsplash.com/photo-1599058917212-d750089bc07d?w=1200"),
                p("Microfiber Towel", "Quick dry gym towel.", "249", 30, Purpose.GYM_PACK, 6, true, "https://images.unsplash.com/photo-1576678927484-cc907957088c?w=1200"),
                p("Skipping Rope", "Speed rope for cardio.", "199", 40, Purpose.GYM_PACK, 6, false, "https://images.unsplash.com/photo-1526403226-6d1b0c9b1c85?w=1200"),

                p("Greeting Card", "Premium birthday card.", "99", 100, Purpose.BIRTHDAY_GIFT, 5, true, "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=1200"),
                p("Chocolate Box", "Assorted chocolates.", "349", 40, Purpose.BIRTHDAY_GIFT, 8, true, "https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?w=1200"),
                p("Scented Candle", "Aromatic candle.", "299", 25, Purpose.BIRTHDAY_GIFT, 7, true, "https://images.unsplash.com/photo-1509726368024-3f6d0e05b6c6?w=1200"),
                p("Gift Wrap Pack", "Wrap paper + ribbon.", "149", 60, Purpose.BIRTHDAY_GIFT, 6, false, "https://images.unsplash.com/photo-1513883049090-d0b7439799bf?w=1200"),
                p("Photo Frame", "6x8 frame.", "399", 30, Purpose.BIRTHDAY_GIFT, 7, false, "https://images.unsplash.com/photo-1520694478161-49d41b0a8b55?w=1200"),

                p("Sticker Pack", "Cute sticker set.", "79", 150, Purpose.HOSTEL_ESSENTIALS, 3, true, "https://images.unsplash.com/photo-1520975916090-3105956dac38?w=1200"),
                p("Keychain", "Metal keychain.", "99", 120, Purpose.BIRTHDAY_GIFT, 3, true, "https://images.unsplash.com/photo-1559239115-ce3eb7cb87ea?w=1200"),
                p("Wrist Band", "Motivation wrist band.", "89", 120, Purpose.GYM_PACK, 3, true, "https://images.unsplash.com/photo-1526401485004-2aa7b7df8f54?w=1200")
        );

        productRepository.saveAll(seed);
    }

    private Product p(String name, String desc, String price, int stock, Purpose purpose, int score, boolean surprise, String img){
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(new BigDecimal(price));
        p.setStock(stock);
        p.setPurpose(purpose);
        p.setUtilityScore(score);
        p.setSurpriseEligible(surprise);
        p.setImageUrl(img);
        return p;
    }
}
