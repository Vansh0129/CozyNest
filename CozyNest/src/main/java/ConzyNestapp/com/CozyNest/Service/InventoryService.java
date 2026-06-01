package ConzyNestapp.com.CozyNest.Service;

import ConzyNestapp.com.CozyNest.Dto.Request.HotelSearchRequest;
import ConzyNestapp.com.CozyNest.Dto.Response.HotelPricingDto;
import ConzyNestapp.com.CozyNest.Entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
@Service
public interface InventoryService {
    Boolean InitializeRoomForOneYear(RoomEntity room);      //hotel-->room--->room's inventory

    Boolean deleteAllInventoryForRoom(RoomEntity room);

    Page<HotelPricingDto> searchHotelWithParam(HotelSearchRequest request);
}
