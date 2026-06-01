package ConzyNestapp.com.CozyNest.Configuration;

import ConzyNestapp.com.CozyNest.Service.ServiceImpl.PricingUpdateService;
import ConzyNestapp.com.CozyNest.strategy.PricingService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class modelMapper {
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }

    @Autowired
    private PricingUpdateService pricingUpdateService;

//    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        pricingUpdateService.updatePrice();
    }
}
