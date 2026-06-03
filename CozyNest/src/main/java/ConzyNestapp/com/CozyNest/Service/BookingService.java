package ConzyNestapp.com.CozyNest.Service;

import ConzyNestapp.com.CozyNest.Dto.BookingDto;
import ConzyNestapp.com.CozyNest.Dto.GuestDto;
import ConzyNestapp.com.CozyNest.Dto.Request.BookingRequest;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface BookingService {

    BookingDto initiateBooking(BookingRequest bookingRequest);

    BookingDto addGuest(Long bookingid, List<GuestDto> guestDtos);

    String paymentInit(Long bookingid);

    void paymentRefund(Long bookingid) throws StripeException;
}
