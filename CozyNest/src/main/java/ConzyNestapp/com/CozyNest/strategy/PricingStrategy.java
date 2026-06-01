package ConzyNestapp.com.CozyNest.strategy;

import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public interface PricingStrategy {
    BigDecimal calculatePrice(InventoryEntity inventory);
}
