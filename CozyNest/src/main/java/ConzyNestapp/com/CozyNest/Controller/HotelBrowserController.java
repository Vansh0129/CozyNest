package ConzyNestapp.com.CozyNest.Controller;

import ConzyNestapp.com.CozyNest.Dto.Request.HotelSearchRequest;
import ConzyNestapp.com.CozyNest.Dto.Response.HotelInfo;
import ConzyNestapp.com.CozyNest.Dto.Response.HotelPricingDto;
import ConzyNestapp.com.CozyNest.Service.HotelService;
import ConzyNestapp.com.CozyNest.Service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowserController {               //tested
    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @RequestMapping("/Search")
    Page<HotelPricingDto> SearchBar(@RequestBody HotelSearchRequest request){
        return inventoryService.searchHotelWithParam(request);
    }
    @RequestMapping("/{HotelId}/info")
    public ResponseEntity<HotelInfo> GetHotelById(@PathVariable(name="HotelId") Long hotelid){
        return ResponseEntity.ok(hotelService.getHotelInfo(hotelid));
    }


}
