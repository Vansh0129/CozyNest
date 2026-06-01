package ConzyNestapp.com.CozyNest.Repository;

import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.InventoryEntity;
import ConzyNestapp.com.CozyNest.Entity.RoomEntity;
import jakarta.persistence.LockModeType;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity,Long> {
    int deleteByDateAfterAndRoomId(LocalDate date, RoomEntity room);

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
}