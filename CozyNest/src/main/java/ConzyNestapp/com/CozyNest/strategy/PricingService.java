package ConzyNestapp.com.CozyNest.strategy;

import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {
    public BigDecimal calculateDynamicPricing(InventoryEntity inventory){
        PricingStrategy pricingStrategy=new BasicPricingStrategy();
        pricingStrategy=new OccupencyPricing(pricingStrategy);
        pricingStrategy=new UrgencyPricing(pricingStrategy);
//        pricingStrategy=new HolidaysPricing(pricingStrategy);               //good for extenstion and close.
        pricingStrategy=new SurgePriceFactor(pricingStrategy);            //do for seperation of Concern and good coding practice
//{{Check or go throught each chain and apply the changes or the price increment .}}

        return pricingStrategy.calculatePrice(inventory);
    }
}
