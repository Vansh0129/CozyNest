package ConzyNestapp.com.CozyNest.strategy;

import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;

import java.math.BigDecimal;

public class BasicPricingStrategy implements PricingStrategy{

    @Override
    public BigDecimal calculatePrice(InventoryEntity inventory) {
        return inventory.getPrice();
    }
}
