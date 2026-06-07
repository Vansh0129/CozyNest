package ConzyNestapp.com.CozyNest.Service;

import ConzyNestapp.com.CozyNest.Dto.BookingDto;
import ConzyNestapp.com.CozyNest.Dto.HotelDto;
import ConzyNestapp.com.CozyNest.Dto.HotelReport;
import ConzyNestapp.com.CozyNest.Dto.Response.HotelInfo;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface HotelService {
     HotelDto CreateHotel(HotelDto dto);
     List<HotelDto> findAllHotel();
     HotelDto FindById(Long id);
     HotelDto UpdateById(HotelDto hotelDto,Long id);
     void DeleteByID(Long hotelId);


     HttpStatusCode activateHotel(Long hotelId);

    HotelInfo getHotelInfo(Long hotelid);




    List<BookingDto> allConfirmBookingsForHotel(Long hotelid) throws AccessDeniedException;

    List<BookingDto> AllBookingForHotel(Long hotelid) throws AccessDeniedException;

    HotelReport findReport(Long hotelid, LocalDate from, LocalDate to) throws AccessDeniedException;
}
