package ConzyNestapp.com.CozyNest.Controller;

import ConzyNestapp.com.CozyNest.Dto.HotelDto;
import ConzyNestapp.com.CozyNest.Service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/hotels")
@RestController
public class HotelController {


    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDto> CreateHotel(@RequestBody HotelDto dto){
        log.info("Attempting ton create the hotel !");
       HotelDto response= hotelService.CreateHotel(dto);
       return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    @GetMapping("/{hotelid}")
    public ResponseEntity<HotelDto> GetHotelById(@PathVariable Long hotelid){
        HotelDto dto=hotelService.FindById(hotelid);
        return ResponseEntity.ok(dto);

    }
    @PutMapping("/{hotelid}")
    public ResponseEntity<HotelDto> UpdateHotel(@RequestBody HotelDto dto,@PathVariable(name="hotelid") Long hotel_id){
        return new ResponseEntity<>(hotelService.UpdateById(dto,hotel_id),HttpStatus.OK);
    }
    @PatchMapping("/{hotelid}")
    public ResponseEntity<HotelDto> PartialUpdateHotel(@RequestBody Map<String,String> dto, @PathVariable(name="hotelid") Long hotel_id){
        return new ResponseEntity<>(hotelService.PartialUpdateHotel(dto,hotel_id),HttpStatus.OK);
    }

    @DeleteMapping("/{hotelid}/delete")
    public ResponseEntity<HotelDto>  deleteHotelById(@PathVariable(name="hotelid") Long hotel_id){
        HotelDto  response=hotelService.DeleteByID(hotel_id);
        if(response!=null) return new ResponseEntity<>(HttpStatusCode.valueOf(203));
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }


    @PatchMapping("/Activate/{hotelid}")
    public ResponseEntity<HotelDto> activateHotel(@PathVariable(name="hotelid") Long hotel_id) {
            return new ResponseEntity<>(hotelService.activateHotel(hotel_id));
    }


    }
