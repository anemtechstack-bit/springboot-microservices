package com.anem.comboshop.cart;

import java.math.BigDecimal;
import java.util.*;

public class Cart {
    private final Map<Long, CartItem> items = new LinkedHashMap<>();
    public Collection<CartItem> getItems(){ return items.values(); }

    public void add(Long productId, String name, BigDecimal price, int qty){
        if(qty <= 0) return;
        CartItem ex = items.get(productId);
        if(ex == null) items.put(productId, new CartItem(productId, name, price, qty));
        else ex.setQuantity(ex.getQuantity() + qty);
    }

    public void setQuantity(Long productId, int qty){
        if(!items.containsKey(productId)) return;
        if(qty <= 0) items.remove(productId);
        else items.get(productId).setQuantity(qty);
    }

    public void remove(Long productId){ items.remove(productId); }
    public void clear(){ items.clear(); }

    public BigDecimal total(){
        return items.values().stream().map(CartItem::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int countItems(){
        return items.values().stream().mapToInt(CartItem::getQuantity).sum();
    }
}
