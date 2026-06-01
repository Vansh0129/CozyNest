package ConzyNestapp.com.CozyNest.Service;

import ConzyNestapp.com.CozyNest.Dto.RoomDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RoomService {
        RoomDto getRoomById(long room_Id);
        List<RoomDto> getAllRoomHotel(long hotelId);
        RoomDto createRoomForHotel(Long hotelid, RoomDto room);
        RoomDto updateRoomDetails(Long roomId,Map<String,String> updateSource);
        Boolean deleteRoomById(Long roomid);

}
