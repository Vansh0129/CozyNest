package ConzyNestapp.com.CozyNest.Dto.Response;

import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Getter
@Setter
public class InventoryDto {

    private HotelEntity hotelId;
    private LocalDate date;
    private Integer bookedCount;           //like initially the count is zero so user can see full booking sloat available
    private Integer reservedCount;
    private Integer totalCount;
    private BigDecimal surgeFactor;
    private BigDecimal price;
    private BigDecimal basePrice;       //baseprice*surgeFactor
    private String city;
    private Boolean  closed;

}
