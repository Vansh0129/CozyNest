package ConzyNestapp.com.CozyNest.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class HotelContactInfo {
    @Column(unique = true)
    String completeAddress;
    String location;
    String email;
    @Column(unique = true)
    String phoneNumber;
}
