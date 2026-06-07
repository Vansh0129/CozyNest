package ConzyNestapp.com.CozyNest.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
@Getter
@Builder
public class HotelReport {
   private Long bookingCount;
   private BigDecimal totalRevenue;
   private BigDecimal averageRevenue;
}
