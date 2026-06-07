package ConzyNestapp.com.CozyNest.Service.ServiceImpl;

import ConzyNestapp.com.CozyNest.Dto.Request.HotelSearchRequest;
import ConzyNestapp.com.CozyNest.Dto.Request.UpdateInventoryDto;
import ConzyNestapp.com.CozyNest.Dto.Response.HotelPricingDto;
import ConzyNestapp.com.CozyNest.Dto.Response.InventoryDto;
import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import ConzyNestapp.com.CozyNest.Entity.RoomEntity;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import ConzyNestapp.com.CozyNest.Exception.UnAuthorisedException;
import ConzyNestapp.com.CozyNest.Repository.HotelMinPriceRepository;
import ConzyNestapp.com.CozyNest.Repository.InventoryRepository;
import ConzyNestapp.com.CozyNest.Repository.RoomRepository;
import ConzyNestapp.com.CozyNest.Service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static ConzyNestapp.com.CozyNest.Utils.AppUtils.getUser;

@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {
    private final ModelMapper mapper;
    private final Integer PAGE_SIZE=5;
    private final InventoryRepository inventoryRepository;
    private  final HotelMinPriceRepository hotelMinPriceRepository;
    private final  PricingUpdateService pricingUpdateService;
    private final RoomRepository roomRepository;

    @Override
    public Boolean InitializeRoomForOneYear(RoomEntity room) {
        //if hotel is active then only this api been called !
        //need to create the new inventory from today to 1 year initializing with 0count .
        LocalDate today=LocalDate.now();
        LocalDate oneYear=today.plusYears(1L);

        for(;today.isBefore(oneYear);today=today.plusDays(1L)){
            InventoryEntity inventoryEntity=InventoryEntity.builder()
                    .roomId(room)
                    .date(today)
                    .surgeFactor(BigDecimal.ONE)
                    .closed(false)
                    .price(room.getBasePrice())
                    .basePrice(room.getBasePrice())
                    .city(room.getHotelEntity().getCity())
                    .bookedCount(0)         //accessing
                    .reservedCount(0)          //temporart Blocked for booking
                    .totalCount(room.getTotalCount())
                    .hotelId(room.getHotelEntity())
                    .createdAt(room.getCreatedDate())
                    .updatedAt(room.getUpdatedDate())

                    .build();
            inventoryRepository.save(inventoryEntity);
        }return true;
    }



//    @Override
//    public Page<HotelDto> searchHotelWithParam(HotelSearchRequest request) {
//        Pageable page= PageRequest.of(request.getPage(),PAGE_SIZE);
//
//        long days= ChronoUnit.DAYS.between(request.getStartDate(),request.getEndDate());
//        Page<HotelEntity> entity=inventoryRepository.findByHotelAndDate(request.getCity(),request.getStartDate(),request.getEndDate(),request.getRoomCount(),days,page);
//        return entity.map(e->mapper.map(e, HotelDto.class));
//
//    } V1
    @Override
    public Page<HotelPricingDto> searchHotelWithParam(HotelSearchRequest request) {
        if(request.getStartDate().isAfter(request.getEndDate())) throw new IllegalArgumentException("CheckIn booking date must less or equal to  CheckOut booking date" );
        Pageable page= PageRequest.of(request.getPage(),PAGE_SIZE);

        long days= ChronoUnit.DAYS.between(request.getStartDate(),request.getEndDate());
        Page<HotelPricingDto> dto=hotelMinPriceRepository.findInventoryforAllHotel(request.getCity(),request.getStartDate(),request.getEndDate(),page);
        return dto;

    }       //first hotel find request

    @Override
    @Transactional
    public void updateInventoryForRoom(RoomEntity room, BigDecimal basePrice) {

        List<InventoryEntity> entities=inventoryRepository.findAllByRoomId(room);
        for(InventoryEntity entity:entities){
            entity.setBasePrice(basePrice);
        }

        inventoryRepository.saveAll(entities);
         pricingUpdateService.updatePrice();

    }

    @Override
    public List<InventoryDto> getAllInventoryForRoom(Long roomId) {     //only for Admins
        RoomEntity rooms=roomRepository
                .findById(roomId)
                .orElseThrow(()->new IllegalArgumentException("Invalid RoomId"));

        UserEntity user=getUser();
        if(!rooms.getHotelEntity().getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");
        List<InventoryEntity> entities=inventoryRepository.findAllByRoomIdOrderByDate(rooms);

        return entities.stream()
                .map(entitie->mapper.map(entitie, InventoryDto.class))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void UpdateInventory(Long roomId, UpdateInventoryDto updateInventoryDto) {
        RoomEntity rooms=roomRepository
                .findById(roomId)
                .orElseThrow(()->new IllegalArgumentException("Invalid RoomId"));

        UserEntity user=getUser();
        if(!rooms.getHotelEntity().getOwner().equals(user.getUsername())) throw new UnAuthorisedException("The user does not Own this hotel !");
        inventoryRepository.findAndLockAvailableInventory(rooms,updateInventoryDto.getStartDate(),updateInventoryDto.getEndDate(),0L);
        inventoryRepository.findAndUpdateSR(rooms,updateInventoryDto.getStartDate(),updateInventoryDto.getEndDate(),updateInventoryDto.getSurgeFactor());
        this.pricingUpdateService.updatePrice();        //update price!



    }

    @Override
    public void deleteAllInventoryForHotel(HotelEntity hotelId) {


        inventoryRepository.deleteAllByHotelId(hotelId);
    }//when hotel off or any other issues.

    @Override
    public void deleteAllInventoryForRoom(RoomEntity roomEntity) {
        inventoryRepository.deleteAllByRoomId(roomEntity);
    }


}
