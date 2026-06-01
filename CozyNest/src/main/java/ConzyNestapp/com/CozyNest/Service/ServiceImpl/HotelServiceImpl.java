package ConzyNestapp.com.CozyNest.Service.ServiceImpl;


import ConzyNestapp.com.CozyNest.Dto.HotelDto;
import ConzyNestapp.com.CozyNest.Dto.Response.HotelInfo;
import ConzyNestapp.com.CozyNest.Dto.RoomDto;
import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.RoomEntity;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import ConzyNestapp.com.CozyNest.Exception.ResourceNotFoundException;
import ConzyNestapp.com.CozyNest.Repository.HotelRepository;
import ConzyNestapp.com.CozyNest.Repository.RoomRepository;
import ConzyNestapp.com.CozyNest.Service.HotelService;
import ConzyNestapp.com.CozyNest.Service.InventoryService;
import ConzyNestapp.com.CozyNest.Service.RoomService;
import ConzyNestapp.com.CozyNest.Exception.UnAuthorisedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class HotelServiceImpl implements HotelService {

    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;
    private final RoomService roomService;

    @Override
    public HotelDto CreateHotel(HotelDto dto) {
        log.info("Creating hotel with name {}",dto.getName());
        HotelEntity hotelEntity= modelMapper.map(dto,HotelEntity.class);
        UserEntity user=(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotelEntity.setOwner(user.getUsername());
        hotelEntity= hotelRepository.save(hotelEntity);
       log.info("Hotel created with id {}",hotelEntity.getId());


      return  modelMapper.map(hotelEntity,HotelDto.class);
    }

    @Override
    public HotelDto FindById(Long id) {

        HotelEntity entity=hotelRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("Cannot find Hotel with id "+id));

        return modelMapper.map(entity,HotelDto.class);
    }

    @Override
    @Transactional              //Hibernate take care of this
    public HotelDto UpdateById(HotelDto hotelDto, Long id) {
        HotelEntity hotel= hotelRepository.findById(id)
                        .orElseThrow(()->new ResourceNotFoundException("Cannot find hotel with id "+id));
        UserEntity user=(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!hotel.getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");
        modelMapper.map(hotelDto,hotel);
        hotel.setId(id);
        return modelMapper.map(hotel,HotelDto.class);


    }

    @Override
    @Transactional
    public HotelDto DeleteByID(Long hotelId) {
        HotelEntity hotel= hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find hotel with id "+hotelId));
        UserEntity user=(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!hotel.getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");
        log.info("Deleting the Hotel with id {}",hotelId);

        for(RoomEntity room:hotel.getRooms()){
            inventoryService.deleteAllInventoryForRoom(room);
            roomRepository.delete(room);
        }
        hotelRepository.deleteById(hotelId);
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    @Transactional
    public HotelDto PartialUpdateHotel(Map<String, String> dto, Long hotelId) {
        HotelEntity hotel= hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find hotel with id "+hotelId));
        UserEntity user=(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!hotel.getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");
        modelMapper.map(dto,hotel);
        return this.FindById(hotelId);
    }

    @Override
    @Transactional
    public HttpStatusCode activateHotel(Long hotelId) {
        HotelEntity hotel=hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Invalid hotel id "+ hotelId));

        hotel.setActive(true);
//        hotelRepository.save(hotel);
        UserEntity user=(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!hotel.getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");
        for(RoomEntity room:hotel.getRooms()){
            inventoryService.InitializeRoomForOneYear(room);                //activate hotel room one by one
        }

        return HttpStatusCode.valueOf(200);
    }

    @Override
    public HotelInfo getHotelInfo(Long hotelid) {

        List<RoomDto> rooms=roomService.getAllRoomHotel(hotelid);
        HotelDto hotel =modelMapper.map(hotelRepository.findById(hotelid),HotelDto.class);
        return new HotelInfo(hotel,rooms);

    }

}
