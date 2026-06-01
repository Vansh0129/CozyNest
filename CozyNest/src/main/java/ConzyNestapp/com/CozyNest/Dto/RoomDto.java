package ConzyNestapp.com.CozyNest.Dto;

import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    private Long id;
    private String type;
    private BigDecimal basePrice;  //base_price numeric(38,2) not null,--> by default 32 Integer and 2 Decimal value
    @Column(columnDefinition = "TEXT[]")
    private String[] photo;
    @Column(columnDefinition = "TEXT[]")
    private String[] amenities;
    private Integer totalCount;
    private Integer capacity;
}
