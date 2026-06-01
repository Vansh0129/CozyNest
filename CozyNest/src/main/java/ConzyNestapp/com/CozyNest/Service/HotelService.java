package ConzyNestapp.com.CozyNest.Service;

import ConzyNestapp.com.CozyNest.Dto.HotelDto;
import ConzyNestapp.com.CozyNest.Dto.Response.HotelInfo;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface HotelService {
     HotelDto CreateHotel(HotelDto dto);
     HotelDto FindById(Long id);
     HotelDto UpdateById(HotelDto hotelDto,Long id);
     HotelDto DeleteByID(Long hotelId);


     HotelDto PartialUpdateHotel(Map<String, String> dto, Long hotelId);

     HttpStatusCode activateHotel(Long hotelId);
//     TODO:Deactivate Hotel!
    HotelInfo getHotelInfo(Long hotelid);
}
