package ConzyNestapp.com.CozyNest.Entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class HotelContactInfo {
    String completeAddress;
    String location;
    String email;
    String phoneNumber;
}
