package ConzyNestapp.com.CozyNest.Repository;

import ConzyNestapp.com.CozyNest.Entity.BookingEntity;
import ConzyNestapp.com.CozyNest.Entity.Enums.BookingStatus;
import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity,Long> {
    Optional<BookingEntity> findBytransactionId(String id);


    @Query("""
    SELECT b.hotel
    FROM BookingEntity b
    WHERE b.hotel.owner = :username
      AND b.bookingStatus = :status
""")
    List<HotelEntity> findBookedHotelsForOwner(
            @Param("username") String username,
            @Param("status") BookingStatus status
    );

    @Query("""
    SELECT b
    FROM BookingEntity b
    WHERE b.hotel = :hotel
      AND b.bookingStatus = :status
""")
    List<BookingEntity> findConfirmBookingForOwner(@Param("hotel") HotelEntity hotel,
                                        @Param("status") BookingStatus status);

    @Query("""
    SELECT b
    FROM BookingEntity b
    WHERE b.hotel = :hotel
""")
    List<BookingEntity> findAllBookingForHotel(@Param("hotel") HotelEntity hotel);

    @Query("""
    SELECT b
    FROM BookingEntity b
    WHERE b.hotel = :hotel
    AND b.createdAt  BETWEEN :from AND :to
    AND b.bookingStatus = :status
    
""")
    List<BookingEntity> findAllConfirmBookingForCreatedDateBetween(@Param("hotel") HotelEntity hotel,
                                                                @Param("from") LocalDateTime from,
                                                                @Param("to") LocalDateTime to,
                                                                @Param("status")BookingStatus status);
    @Query("""
    SELECT b
    FROM BookingEntity b
    WHERE b.user_Id = :user
    AND b.bookingStatus = :status
""")
    List<BookingEntity> findAllBookingForUser(@Param("user") UserEntity user,
                                              @Param("status") BookingStatus bookingStatus
    );
}
