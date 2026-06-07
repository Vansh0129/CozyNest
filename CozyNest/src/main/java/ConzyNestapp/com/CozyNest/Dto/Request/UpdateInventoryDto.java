package ConzyNestapp.com.CozyNest.Dto.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
public class UpdateInventoryDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal surgeFactor;


}
