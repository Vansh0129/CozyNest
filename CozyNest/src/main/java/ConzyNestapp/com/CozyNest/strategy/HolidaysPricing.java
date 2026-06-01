package ConzyNestapp.com.CozyNest.strategy;

import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class HolidaysPricing implements PricingStrategy {

    private final  PricingStrategy wrapper;

    @Override
    public BigDecimal calculatePrice(InventoryEntity inventory) {

//        Todo:Add the api for determining the Holidays https://api-ninjas.com/api/holidays#ispublicholiday-endpoint   -->https://api.api-ninjas.com/v1/ispublicholiday?country=India&date=2025-01-01
//
        if(holidayFinder(inventory.getDate()))
        {
            log.info("Attempting the Holiday pricing for the date {}",inventory.getDate());
            return wrapper.calculatePrice(inventory).multiply(BigDecimal.valueOf(1.4));
        }
        else   {log.info("Attempting the Normal pricing for the date {}",inventory.getDate());
            return wrapper.calculatePrice(inventory);
        }

    }

    private Boolean  holidayFinder(LocalDate date){     //Following The IN Holidays
        log.info("Holiday api");
        RestClient restClient= RestClient.builder()
                .baseUrl("https://holidays.abstractapi.com/v1?api_key=d420577919d84f59ace9a1ba535465ba&country=IN")     //working good
//                .defaultHeader("X-Api-Key","PPgj7LookeL7fW5BVcRtX29M2xY9Zd7QTCvlXD78")
                .build();
        List holidayApiResponse=restClient.get()
                .uri(String.format("&year=%d&month=%d&day=%d", date.getYear(), date.getMonthValue(), date.getDayOfMonth()))
                .retrieve()
                .body(List.class );

        return holidayApiResponse.isEmpty();
    }
}
///