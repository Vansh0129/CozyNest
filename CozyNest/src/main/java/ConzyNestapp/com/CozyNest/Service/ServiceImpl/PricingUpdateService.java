package ConzyNestapp.com.CozyNest.Service.ServiceImpl;


import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.HotelMinPrice;
import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import ConzyNestapp.com.CozyNest.Repository.HotelMinPriceRepository;
import ConzyNestapp.com.CozyNest.Repository.HotelRepository;
import ConzyNestapp.com.CozyNest.Repository.InventoryRepository;
import ConzyNestapp.com.CozyNest.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

@Transactional
public class PricingUpdateService {
    private final HotelRepository hotelRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final InventoryRepository inventoryRepository;
    private final PricingService pricingService;

//TODO:only need to increse /update price when some event occure like the booking made/surgeFactor increase then only.
    @Scheduled(cron = "0 0 * * * *")    //"0 0 * * * *" -> for each hr, "0 0 */5 * * *" -> each 5 hr. and do so onn (checked from website croneHub)
    //There are many way! for schedular//
    public void updatePrice(){
        int page=0;
        int batchSize=100;
        while(true){
            Page<HotelEntity> hotelPage=hotelRepository.findAll(PageRequest.of(page,batchSize));
            if(hotelPage.isEmpty()) break;
            log.info("page No is : {}",page);

            hotelPage.getContent().forEach(hotel->updateHotelPrice( hotel));
            page++;

        }

    }

    private void updateHotelPrice(HotelEntity hotel) {              //Doing:getting hotels and for each hotel updating the min price value for easy log.
        LocalDate startDate=LocalDate.now();
        LocalDate endDate=startDate.plusMonths(1);      //also can update price for the 1 year
        List<InventoryEntity> inventoryList =inventoryRepository.findByHotelIdAndDateBetween(hotel,startDate,endDate);  //finding inventory for this hotel during start and end Dates.
        updateInventoryPrice(inventoryList);            //Inventory Db value updated ;


        updateHotelMinPrice(hotel,inventoryList,startDate,endDate);     //for given inventory of hotel  at that duration find inventory for each room and set min value Price
        //And for that date update the hotel min price of the hotelMinPrice Repo



    }

    private void updateHotelMinPrice(HotelEntity hotel,List<InventoryEntity> inventoryList, LocalDate startDate, LocalDate endDate) {
            Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
                    .collect(Collectors.groupingBy(
                            InventoryEntity::getDate,
                            Collectors.mapping(
                                    InventoryEntity::getPrice,
                                    Collectors.minBy(Comparator.naturalOrder())
                            )
                    ))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().orElse(BigDecimal.ZERO)
                    ));

            List<HotelMinPrice> hotelPrices = new ArrayList<>();

            dailyMinPrices.forEach((date, price) -> {
                HotelMinPrice hotelPrice = hotelMinPriceRepository
                        .findByHotelAndDate(hotel, date)
                        .orElse(new HotelMinPrice(hotel, date));

                hotelPrice.setPrice(price);
                hotelPrices.add(hotelPrice);
            });

            hotelMinPriceRepository.saveAll(hotelPrices);
        }



    private void updateInventoryPrice(List<InventoryEntity> inventoryList) {
        inventoryList.forEach(inventory -> {
            BigDecimal price =pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(price);          //TODO:this can repetadely increse the calculated price for same event so need set BAse price entity and price differently! at first time both will be same

        });
        inventoryRepository.saveAll(inventoryList);

        log.info("Exiting Cron work done !");


    }
}

