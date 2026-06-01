package ConzyNestapp.com.CozyNest.Dto.Response;


import ConzyNestapp.com.CozyNest.Dto.HotelDto;
import ConzyNestapp.com.CozyNest.Dto.RoomDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HotelInfo {
    HotelDto hotel;
    List<RoomDto> roomList;


}
