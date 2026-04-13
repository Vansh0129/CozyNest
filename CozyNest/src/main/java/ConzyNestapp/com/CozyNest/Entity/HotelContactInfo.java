package ConzyNestapp.com.CozyNest.Entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class HotelContactInfo {
    String completeAddress;
    String location;
    String email;
    String phoneNumber;
}
