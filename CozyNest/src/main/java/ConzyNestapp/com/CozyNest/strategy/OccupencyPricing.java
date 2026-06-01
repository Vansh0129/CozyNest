package ConzyNestapp.com.CozyNest.strategy;

import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OccupencyPricing implements PricingStrategy {

    private final PricingStrategy wrapper;

    @Override
    public BigDecimal calculatePrice(InventoryEntity inventory) {
        double occupancy = (double) inventory.getBookedCount() /inventory.getTotalCount();
        if(occupancy >=0.8){
            return wrapper.calculatePrice(inventory).multiply(BigDecimal.valueOf(1.5));
        }return inventory.getPrice();
    }
}
