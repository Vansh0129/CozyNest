package ConzyNestapp.com.CozyNest.Controller;

import ConzyNestapp.com.CozyNest.Dto.BookingDto;
import ConzyNestapp.com.CozyNest.Dto.HotelDto;
import ConzyNestapp.com.CozyNest.Dto.HotelReport;
import ConzyNestapp.com.CozyNest.Service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/hotels")
@RestController
public class HotelController {
    private final HotelService hotelService;            //Tested

    @PostMapping
    public ResponseEntity<HotelDto> CreateHotel(@RequestBody HotelDto dto){     //tested
        log.info("Attempting to create the hotel !");
       HotelDto response= hotelService.CreateHotel(dto);
       return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/All")
    public ResponseEntity<List<HotelDto>> GetAllHotels(){           //new,tested
        List<HotelDto> dtoList=hotelService.findAllHotel();
        return ResponseEntity.ok(dtoList);

    }

    @GetMapping("/{hotelid}")
    public ResponseEntity<HotelDto> GetHotelById(@PathVariable Long hotelid){  //tested
        HotelDto dto=hotelService.FindById(hotelid);
        return ResponseEntity.ok(dto);

    }
    @PutMapping("/{hotelid}")                   //tested
    public ResponseEntity<HotelDto> UpdateHotel(@RequestBody HotelDto dto,@PathVariable(name="hotelid") Long hotel_id){
        return new ResponseEntity<>(hotelService.UpdateById(dto,hotel_id),HttpStatus.OK);
    }
//patch mapping Not recommended because:-No validation,No type safety,Security concerns;

    @DeleteMapping("/{hotelid}/delete")   //tested
    public ResponseEntity<Void>  deleteHotelById(@PathVariable(name="hotelid") Long hotel_id){
        hotelService.DeleteByID(hotel_id);
        return  ResponseEntity.ok(null);
    }


    @PatchMapping("/Activate/{hotelid}")  //tested
    public ResponseEntity<HotelDto> activateHotel(@PathVariable(name="hotelid") Long hotel_id) {
            return new ResponseEntity<>(hotelService.activateHotel(hotel_id));
    }

    @GetMapping("/{Hotelid}/allBookings")      //tested
    public ResponseEntity<List<BookingDto>> AllBookingForHotel(@PathVariable(name="Hotelid") Long hotelid) throws AccessDeniedException {
        List<BookingDto> dtoList=hotelService.AllBookingForHotel(hotelid);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{Hotelid}/bookings")  //new    //tested
    public ResponseEntity<List<BookingDto>> allConfirmBookingForHotel(@PathVariable(name="Hotelid") Long hotelid) throws AccessDeniedException {
        List<BookingDto> dtoList=hotelService.allConfirmBookingsForHotel(hotelid);
        return ResponseEntity.ok(dtoList);
    }
    @GetMapping("/{Hotelid}/report")  ////tested
    public ResponseEntity<HotelReport> getHotelReport(
            @PathVariable(name="Hotelid") Long hotelid ,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) throws AccessDeniedException {

        if(from==null) from=LocalDate.now();
        if(to==null) to=from.plusMonths(1L);
        HotelReport report= hotelService.findReport(hotelid,from,to);
        return ResponseEntity.ok(report);


    }



}
