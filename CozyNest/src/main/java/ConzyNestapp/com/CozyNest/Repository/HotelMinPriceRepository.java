package ConzyNestapp.com.CozyNest.Repository;


import ConzyNestapp.com.CozyNest.Dto.Response.HotelPricingDto;
import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice,Long> {
    //Not checking available room but giving basic price
    @Query("""
      SELECT new ConzyNestapp.com.CozyNest.Dto.Response.HotelPricingDto(i.hotel,AVG(i.price))
      FROM HotelMinPrice i
      WHERE i.hotel.city=:city
      AND i.date BETWEEN :startDate AND :endDate
      AND i.hotel.active=true
      GROUP BY i.hotel
    """)
     Page<HotelPricingDto> findInventoryforAllHotel(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );


    Optional<HotelMinPrice> findByHotelAndDate(HotelEntity hotel, LocalDate date);
}
