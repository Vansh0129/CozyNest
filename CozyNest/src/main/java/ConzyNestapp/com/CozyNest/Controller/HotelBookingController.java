package ConzyNestapp.com.CozyNest.Controller;


import ConzyNestapp.com.CozyNest.Dto.BookingDto;
import ConzyNestapp.com.CozyNest.Dto.GuestDto;
import ConzyNestapp.com.CozyNest.Dto.Request.BookingRequest;
import ConzyNestapp.com.CozyNest.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}
