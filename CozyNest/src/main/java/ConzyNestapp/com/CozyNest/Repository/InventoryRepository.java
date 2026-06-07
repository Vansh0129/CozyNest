package ConzyNestapp.com.CozyNest.Repository;

import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import ConzyNestapp.com.CozyNest.Entity.RoomEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int deleteAllByRoomId( RoomEntity room);

    @Query("""
      SELECT DISTINCT i.hotelId FROM InventoryEntity i
      WHERE i.city=:city
      AND i.date BETWEEN :startDate AND :endDate
      AND i.closed=false
      AND (i.totalCount-i.bookedCount-i.reservedCount>= :roomCount)
      GROUP BY i.hotelId,i.roomId
      HAVING COUNT(i.date)>=:dateCount
    """)
    Page<HotelEntity> findHotelWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount,
            @Param(("dateCount")) Long days,
            Pageable pageable
    );
    //    --Booking RESERVED ---- Query

    @Query("""
      SELECT i FROM InventoryEntity i
      WHERE i.roomId=:roomId
      AND i.date BETWEEN :startDate AND :endDate
      AND i.closed=false
      AND (i.totalCount-i.bookedCount-i.reservedCount>= :roomCount)
    """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
       List<InventoryEntity> findAndLockAvailableInventory(        //gives the list of the inventory available for the room At Given Date range
            @Param("roomId") RoomEntity roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") long roomCount);

    List<InventoryEntity> findByHotelIdAndDateBetween(HotelEntity hotel, LocalDate startDate, LocalDate endDate);

//    --Booking Conform ---- Query

    @Modifying              /// cannot lock the table as Modifing Query
    @Query("""
      Update InventoryEntity i
          Set i.reservedCount=i.reservedCount-:roomCount,
              i.bookedCount=i.bookedCount+:roomCount
      WHERE i.roomId=:roomId
      AND i.date BETWEEN :startDate AND :endDate
      AND i.closed=false
      AND (i.totalCount-i.bookedCount>= :roomCount)
      AND (i.reservedCount>=:roomCount)
    """)
    void ConfirmBooking(@Param("roomId") RoomEntity roomId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("roomCount") long roomCount);



    @Query("""
      SELECT i FROM InventoryEntity i
      WHERE i.roomId=:roomId
      AND i.date BETWEEN :startDate AND :endDate
      AND i.closed=false
      AND i.totalCount-i.reservedCount>= :roomCount
    """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<InventoryEntity> findAndLockReservedInventory(        //gives the list of the  reserved inventory  for the given Booking date range must Lock before Edititng the DB
                    @Param("roomId") RoomEntity roomId,
                    @Param("startDate") LocalDate startDate,
                    @Param("endDate") LocalDate endDate,
                    @Param("roomCount") long roomCount
    );

    @Modifying              /// cannot lock the table as Modifing Query
    @Query("""
      Update InventoryEntity i
          Set i.bookedCount=i.bookedCount-:roomCount
      WHERE i.roomId=:roomId
      AND i.date BETWEEN :startDate AND :endDate
      AND i.closed=false
      AND (i.totalCount>= :roomCount)
    """)
    void refundBooking(@Param("roomId") RoomEntity roomId,
                       @Param("startDate") LocalDate startDate,
                       @Param("endDate") LocalDate endDate,
                       @Param("roomCount") long roomCount);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<InventoryEntity> findAllByRoomId(RoomEntity room);

    List<InventoryEntity> findAllByRoomIdOrderByDate(RoomEntity roomId);


    @Modifying
    @Query("""
      Update InventoryEntity i
          Set i.surgeFactor= :surgeFactor
      WHERE i.roomId=:roomId
      AND i.date BETWEEN :startDate AND :endDate
      AND i.closed=false
    """)
    void findAndUpdateSR(@Param("roomId") RoomEntity roomId,
                         @Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate,
                         @Param("surgeFactor") BigDecimal surgeFactor
    );

    void deleteAllByHotelId(HotelEntity hotelId);
}