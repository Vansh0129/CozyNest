package ConzyNestapp.com.CozyNest.Dto;

import ConzyNestapp.com.CozyNest.Entity.HotelContactInfo;
import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HotelDto {
    private Long id;
    private String name;
    private String city;
    private String[] photo;
    private String[] amenities;
    private HotelContactInfo contactInfo;
    private boolean active=false;
    @Nullable
    private String owner;

}
