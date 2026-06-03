package ConzyNestapp.com.CozyNest.Controller;


import ConzyNestapp.com.CozyNest.Dto.BookingDto;
import ConzyNestapp.com.CozyNest.Dto.GuestDto;
import ConzyNestapp.com.CozyNest.Dto.Request.BookingRequest;
import ConzyNestapp.com.CozyNest.Service.BookingService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/booking")
@RequiredArgsConstructor
@RestController
public class HotelBookingController {           //booking related api's
    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initiateBooking(@RequestBody BookingRequest bookingRequest){
       return ResponseEntity.ok( bookingService.initiateBooking(bookingRequest));

    }
    @PostMapping("/{bookingid}/Guest")
    public ResponseEntity<BookingDto> addGuest(@PathVariable Long bookingid, @RequestBody List<GuestDto> guestDtos){
        return ResponseEntity.ok( bookingService.addGuest(bookingid,guestDtos));

    }

    @PostMapping("/{bookingid}/payment")
    public ResponseEntity<Map> paymentInit(@PathVariable Long bookingid){   //only need the booking id.,And response only get the session url
        String SessionUrl=bookingService.paymentInit(bookingid);
        return ResponseEntity.ok(Map.of("sessionUrl",SessionUrl) );

    }

    @PostMapping("/{bookingid}/refund")
    public ResponseEntity<Void> paymentRefund(@PathVariable Long bookingid) throws StripeException {   //only need the booking id.,And response only get the session url
        bookingService.paymentRefund(bookingid);
        return ResponseEntity.noContent().build();

    }


}
