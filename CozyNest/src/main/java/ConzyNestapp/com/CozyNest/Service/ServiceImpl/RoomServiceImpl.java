package ConzyNestapp.com.CozyNest.Service.ServiceImpl;

import ConzyNestapp.com.CozyNest.Dto.RoomDto;
import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.RoomEntity;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import ConzyNestapp.com.CozyNest.Exception.ResourceNotFoundException;
import ConzyNestapp.com.CozyNest.Repository.HotelRepository;
import ConzyNestapp.com.CozyNest.Repository.RoomRepository;
import ConzyNestapp.com.CozyNest.Service.InventoryService;
import ConzyNestapp.com.CozyNest.Service.RoomService;
import ConzyNestapp.com.CozyNest.Exception.UnAuthorisedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryService inventoryService;


    @Override
    public RoomDto getRoomById( long room_Id) {
        RoomEntity entity=roomRepository.findById(room_Id)
                .orElseThrow(()-> new ResourceNotFoundException("Cannot find hotel Room with Id "+room_Id));
        return modelMapper.map(entity,RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomHotel(long hotelId) {
        HotelEntity hotelEntity=hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Cannot find hotel with Id "+hotelId));
        List<RoomEntity> hotelRooms=hotelEntity.getRooms();
        return hotelRooms
                .stream().map(e->
                        modelMapper.map(e,RoomDto.class))
                        .collect(Collectors.toList());
    }

    @Override
    public RoomDto createRoomForHotel(Long hotelId, RoomDto room) {
        HotelEntity hotelEntity=hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Cannot find hotel with Id "+hotelId));
        UserEntity user=(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!hotelEntity.getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");

        RoomEntity roomEntity=modelMapper.map(room,RoomEntity.class);
        roomEntity.setHotelEntity(hotelEntity);
        roomEntity=roomRepository.save(roomEntity);

//       TODO: as created the room also create the Inventory if hotelIsActive.
        if(hotelEntity.isActive()){
            inventoryService.InitializeRoomForOneYear(roomEntity);
        }
        return modelMapper.map(roomEntity,RoomDto.class);

    }       //also no need to take the hotelId

    @Override
    @Transactional
    public RoomDto updateRoomDetails( Long roomId, Map<String,String> updateSource) {
        log.info("Creating changes for hotel Room id {}",roomId);
        RoomEntity room=roomRepository.findById(roomId)
                        .orElseThrow(()->new ResourceNotFoundException(" No room with id "+roomId));
        UserEntity user=(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!room.getHotelEntity().getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");

        modelMapper.map(updateSource,room);
        log.info("new Entity \n {}",room);
        roomRepository.save(room);

        return modelMapper.map(room,RoomDto.class);


    }

    @Override
    public Boolean deleteRoomById( Long roomid) {

        RoomEntity roomEntity=roomRepository
                .findById(roomid)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find the Room with id "+roomid));
        UserEntity user=(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!roomEntity.getHotelEntity().getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");


        inventoryService.deleteAllInventoryForRoom(roomEntity);
        roomRepository.delete(roomEntity);
        return  true;

    }

}
