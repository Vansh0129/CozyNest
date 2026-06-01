package ConzyNestapp.com.CozyNest.strategy;

import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UrgencyPricing implements PricingStrategy{
    private final PricingStrategy wrapper;

    @Override
    public BigDecimal calculatePrice(InventoryEntity inventory) {
        if(LocalDateTime.now().plusDays(7).isBefore(inventory.getDate().atStartOfDay())){
            return wrapper.calculatePrice(inventory).multiply(BigDecimal.valueOf(1.3));
        }
        return inventory.getPrice();
    }
}
