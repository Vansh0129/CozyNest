package ConzyNestapp.com.CozyNest.Service;

import ConzyNestapp.com.CozyNest.Dto.Request.HotelSearchRequest;
import ConzyNestapp.com.CozyNest.Dto.Request.UpdateInventoryDto;
import ConzyNestapp.com.CozyNest.Dto.Response.HotelPricingDto;
import ConzyNestapp.com.CozyNest.Dto.Response.InventoryDto;
import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface InventoryService {
    Boolean InitializeRoomForOneYear(RoomEntity room);      //hotel-->room--->room's inventory

    Page<HotelPricingDto> searchHotelWithParam(HotelSearchRequest request);

    void updateInventoryForRoom(RoomEntity room, BigDecimal basePrice);

    List<InventoryDto> getAllInventoryForRoom(Long roomId);

    void UpdateInventory(Long roomId, UpdateInventoryDto updateInventoryDto);

    void deleteAllInventoryForHotel(HotelEntity hotelId);

    void deleteAllInventoryForRoom(RoomEntity roomEntity);
}
