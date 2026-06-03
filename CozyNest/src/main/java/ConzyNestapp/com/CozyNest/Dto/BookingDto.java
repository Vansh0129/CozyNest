package ConzyNestapp.com.CozyNest.Dto;

import ConzyNestapp.com.CozyNest.Entity.Enums.BookingStatus;
import ConzyNestapp.com.CozyNest.Entity.GuestEntity;
import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import ConzyNestapp.com.CozyNest.Entity.RoomEntity;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data

public class BookingDto {
    private Long booking_id;
    private BigDecimal price;
    @JsonIgnore
    private HotelEntity hotel;
    @JsonIgnore
    private RoomEntity room;

    private UserEntity user_Id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private Long room_Count;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Set<GuestDto> guests;
}
