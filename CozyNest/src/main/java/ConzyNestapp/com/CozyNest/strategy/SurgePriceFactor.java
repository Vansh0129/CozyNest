package ConzyNestapp.com.CozyNest.strategy;

import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class SurgePriceFactor implements PricingStrategy{
    private final PricingStrategy wrapper;
    private final BigDecimal SURGE_FACTOR= BigDecimal.valueOf(1.1);

    @Override
    public BigDecimal calculatePrice(InventoryEntity inventory) {
        return wrapper.calculatePrice(inventory).multiply(SURGE_FACTOR);
    }
}
