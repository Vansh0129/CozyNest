package ConzyNestapp.com.CozyNest.Dto.Response;


import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
@RequiredArgsConstructor
public class HotelPricingDto {
    private HotelEntity hotel;        //for the hotel
    private BigDecimal price;       //cheapest room of Day
    public HotelPricingDto(HotelEntity hotel, Double price) {
        this.hotel=hotel;
        this.price=BigDecimal.valueOf(price);
    }



}
