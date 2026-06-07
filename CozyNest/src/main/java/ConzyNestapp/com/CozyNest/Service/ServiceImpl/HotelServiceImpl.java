package ConzyNestapp.com.CozyNest.Service.ServiceImpl;


import ConzyNestapp.com.CozyNest.Dto.BookingDto;
import ConzyNestapp.com.CozyNest.Dto.HotelDto;
import ConzyNestapp.com.CozyNest.Dto.HotelReport;
import ConzyNestapp.com.CozyNest.Dto.Response.HotelInfo;
import ConzyNestapp.com.CozyNest.Dto.RoomDto;
import ConzyNestapp.com.CozyNest.Entity.BookingEntity;
import ConzyNestapp.com.CozyNest.Entity.Enums.BookingStatus;
import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.RoomEntity;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import ConzyNestapp.com.CozyNest.Exception.ResourceNotFoundException;
import ConzyNestapp.com.CozyNest.Exception.UnAuthorisedException;
import ConzyNestapp.com.CozyNest.Repository.BookingRepository;
import ConzyNestapp.com.CozyNest.Repository.HotelRepository;
import ConzyNestapp.com.CozyNest.Repository.RoomRepository;
import ConzyNestapp.com.CozyNest.Service.HotelService;
import ConzyNestapp.com.CozyNest.Service.InventoryService;
import ConzyNestapp.com.CozyNest.Service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ConzyNestapp.com.CozyNest.Utils.AppUtils.getUser;

@RequiredArgsConstructor
@Slf4j
@Service
public class HotelServiceImpl implements HotelService {

    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;

    @Override
    public HotelDto CreateHotel(HotelDto dto) {
        log.info("Creating hotel with name {}",dto.getName());
        HotelEntity hotelEntity= modelMapper.map(dto,HotelEntity.class);
        UserEntity user=getUser();
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
        UserEntity user=getUser();
        if(!hotel.getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");
        modelMapper.map(hotelDto,hotel);
        hotel.setOwner(user.getUsername());
        hotel.setId(id);
        return modelMapper.map(hotel,HotelDto.class);


    }

    @Override
    @Transactional
    public void DeleteByID(Long hotelId) {
        HotelEntity hotel= hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find hotel with id "+hotelId));
        UserEntity user=getUser();
        if(!hotel.getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");
        log.info("Deleting the Hotel with id {}",hotelId);
//
//        for(RoomEntity room:hotel.getRooms()){
//            inventoryService.deleteAllInventoryForRoom(room);
//            roomRepository.delete(room);
//        }
        inventoryService.deleteAllInventoryForHotel(hotel);
        hotelRepository.deleteById(hotelId);
    }



    @Override
    @Transactional
    public HttpStatusCode activateHotel(Long hotelId) {
        HotelEntity hotel=hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Invalid hotel id "+ hotelId));

        hotel.setActive(true);
//        hotelRepository.save(hotel);
        UserEntity user=getUser();
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

    @Override
    public List<BookingDto> allConfirmBookingsForHotel(Long hotelid) throws AccessDeniedException {
        UserEntity user=getUser();
        HotelEntity hotel=hotelRepository.findById(hotelid).orElseThrow(()->new ResourceNotFoundException("Invalid hotel id "+ hotelid));

        if(!user.getUsername().equals(hotel.getOwner())) throw new AccessDeniedException("The user does not own this hotel !");
        List<BookingEntity> bookingEntities=bookingRepository.findConfirmBookingForOwner(hotel, BookingStatus.CONFIRMED);

        return bookingEntities.stream()
                .map(hotelEntity -> modelMapper.map(hotelEntity,BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> AllBookingForHotel(Long hotelid) throws AccessDeniedException {
        UserEntity user=getUser();
        HotelEntity hotel=hotelRepository.findById(hotelid).orElseThrow(()->new ResourceNotFoundException("Invalid hotel id "+ hotelid));

        if(!user.getUsername().equals(hotel.getOwner())) throw new AccessDeniedException("The user does not own this hotel !");
        List<BookingEntity> bookingEntities=bookingRepository.findAllBookingForHotel(hotel);

        return bookingEntities.stream()
                .map(hotelEntity -> modelMapper.map(hotelEntity,BookingDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public HotelReport findReport(Long hotelid, LocalDate from, LocalDate to) throws AccessDeniedException {
        UserEntity user=getUser();
        HotelEntity hotel=hotelRepository.findById(hotelid).orElseThrow(()->new ResourceNotFoundException("Invalid hotel id "+ hotelid));

        if(!user.getUsername().equals(hotel.getOwner())) throw new AccessDeniedException("The user does not own this hotel !");


        LocalDateTime start=from.atStartOfDay();
        LocalDateTime end=to.atTime(LocalTime.of(23,59));


        List<BookingEntity> bookingEntities=bookingRepository.findAllConfirmBookingForCreatedDateBetween(hotel,start,end,BookingStatus.CONFIRMED);
        Long totalBooking= (long) bookingEntities.size();
        BigDecimal totalRevenue=BigDecimal.valueOf(0);
        bookingEntities.stream().map(booking -> totalRevenue.add(booking.getPrice()));
        BigDecimal avegrageRevenue=totalBooking==0? BigDecimal.ZERO:totalRevenue.divide(BigDecimal.valueOf(totalBooking));
        return HotelReport.builder()
                .totalRevenue(totalRevenue)
                .averageRevenue(avegrageRevenue)
                .bookingCount(totalBooking)
                .build();







    }


    @Override
    public List<HotelDto> findAllHotel() {
        UserEntity user=getUser();
        List<HotelEntity> listHotel=hotelRepository.findAllByOwner(user.getUsername());
        
        return listHotel.stream()
                .map(hotelEntity -> modelMapper.map(hotelEntity,HotelDto.class))
                .collect(Collectors.toList());

    }

}
