package ConzyNestapp.com.CozyNest.Service.ServiceImpl;

import ConzyNestapp.com.CozyNest.Dto.Request.HotelSearchRequest;
import ConzyNestapp.com.CozyNest.Dto.Response.HotelPricingDto;
import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import ConzyNestapp.com.CozyNest.Entity.RoomEntity;
import ConzyNestapp.com.CozyNest.Repository.HotelMinPriceRepository;
import ConzyNestapp.com.CozyNest.Repository.InventoryRepository;
import ConzyNestapp.com.CozyNest.Service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {
    private final ModelMapper mapper;
    private final Integer PAGE_SIZE=5;
    private final InventoryRepository inventoryRepository;
    private  final HotelMinPriceRepository hotelMinPriceRepository;

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

    @Override
    public Boolean deleteAllInventoryForRoom(RoomEntity room) {
       int count= inventoryRepository.deleteByDateAfterAndRoomId(LocalDate.now(),room);
        return count>0 ;

    }   //when hotel off or any other issues.

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
        Pageable page= PageRequest.of(request.getPage(),PAGE_SIZE);

        long days= ChronoUnit.DAYS.between(request.getStartDate(),request.getEndDate());
        Page<HotelPricingDto> dto=hotelMinPriceRepository.findInventoryforAllHotel(request.getCity(),request.getStartDate(),request.getEndDate(),page);
        return dto;

    }       //first hotel find request


}
