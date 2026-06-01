package ConzyNestapp.com.CozyNest.Service.ServiceImpl;


import ConzyNestapp.com.CozyNest.Dto.BookingDto;
import ConzyNestapp.com.CozyNest.Dto.GuestDto;
import ConzyNestapp.com.CozyNest.Dto.Request.BookingRequest;
import ConzyNestapp.com.CozyNest.Entity.*;
import ConzyNestapp.com.CozyNest.Entity.Enums.BookingStatus;
import ConzyNestapp.com.CozyNest.Exception.ResourceNotFoundException;
import ConzyNestapp.com.CozyNest.Repository.*;
import ConzyNestapp.com.CozyNest.Service.BookingService;
import ConzyNestapp.com.CozyNest.Exception.UnAuthorisedException;
import ConzyNestapp.com.CozyNest.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final  BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelRepository hotelRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;
    private final PricingService pricingService;
    private final Integer PAGE_SIZE=5;

    @Override
    @Transactional
    public BookingDto initiateBooking(BookingRequest bookingRequest) {  //get hotel->room->get available inventory;

        HotelEntity hotel=hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(()-> new ResourceNotFoundException("Cannot find Hotel with Id : "+bookingRequest.getHotelId()));

        RoomEntity room=roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(()-> new ResourceNotFoundException("Cannot find room with Id : "+bookingRequest.getRoomId()));

        List<InventoryEntity> inventoryList=inventoryRepository       //find available inventory for given parameter for same room
                .findAndLockAvailableInventory(room
                        ,bookingRequest.getCheckInDate()
                        ,bookingRequest.getCheckOutDate()
                        ,bookingRequest.getRoomCount());
        long TotalDaysRoomNeedFor= ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate())+1;
        if(inventoryList.size()<TotalDaysRoomNeedFor) throw new IllegalArgumentException("Room With Id "+bookingRequest.getRoomId()+" is Not Available !");


        //Room Available with inventory startBooking for given user and update DB of inventory:
        BigDecimal price=BigDecimal.ZERO;
        for(InventoryEntity inventoryEntity:inventoryList){
            inventoryEntity.setReservedCount((int) (inventoryEntity.getReservedCount()+bookingRequest.getRoomCount()));
            price.add(inventoryEntity.getPrice());
        }
        inventoryRepository.saveAll(inventoryList); //updated the Inventory count!


//        *HardCode
//        UserEntity user=new UserEntity();
//        user.setId(1L);         //user must Exist in DB
//        *Spring security  Component
        UserEntity user=(UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();


//        TODO: add dynamic pricing   (Done -> as Cron job update invetory value and this total value been calculated from start to end date for inventory list )

//
        BookingEntity bookingEntity= BookingEntity.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room_Id(room)
                .checkInDate(bookingRequest.getCheckInDate().atStartOfDay())
                .checkOutDate(bookingRequest.getCheckOutDate().atTime(23,59))
                .user_Id(user)
                .room_Count(bookingRequest.getRoomCount())
                .price(BigDecimal.TEN)
                .build();
        bookingEntity=bookingRepository.save(bookingEntity);
        return modelMapper.map(bookingEntity,BookingDto.class);



    }   //VIMP

    @Override
    @Transactional
    public BookingDto addGuest(Long booking_id, List<GuestDto> guestDtos) {
//        ValidateBooking!


        BookingEntity booking=bookingRepository.findById(booking_id).orElseThrow(()->new ResourceNotFoundException("Invalid Booking Id : "+booking_id));
        UserEntity currentUser=(UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!booking.getUser_Id().getName().equals(currentUser.getName()) || !booking.getUser_Id().getEmail().equals(currentUser.getEmail())) throw new UnAuthorisedException("Booking does not belong to this User !");

    if(booking.getBookingStatus()!=BookingStatus.RESERVED) throw new IllegalArgumentException("Booking is not under RESERVED state ,cannot add guest !");
        if(booking.getCheckOutDate().isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Booking has already expired!");
        Set<GuestEntity> set=new HashSet<>();
        for(GuestDto guest:guestDtos){
            GuestEntity entity=modelMapper.map(guest, GuestEntity.class);
            entity.setUserId(booking.getUser_Id());
            entity=guestRepository.save(entity);
            set.add(entity);
        }
        set.addAll(booking.getGuests());
        booking.setGuests(set);
        bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDto.class);

    }
}
