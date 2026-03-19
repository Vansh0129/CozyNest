package ConzyNestapp.com.CozyNest.Entity;

import ConzyNestapp.com.CozyNest.Entity.Enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="Booking") //hibernate convert the Booking to booking
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false,name="hotel_Id")
    @ManyToOne(fetch = FetchType.LAZY)
    private HotelEntity hotel;

    @JoinColumn(nullable = false,name="room_Id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RoomEntity room_Id;

    @JoinColumn(nullable = false,name="user_Id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user_Id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false,name="booking_Status")
    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column(nullable = false,name="bookedRooms")
    private Long room_Count;

    @Column(nullable = false)
    private LocalDateTime checkInDate;

    @Column(nullable = false)
    private LocalDateTime checkOutDate;

    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(nullable = false)         //Initially it should be nullable
    private PaymentEntity paymentId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="booking_guest",                                //name of Combined table inside db
            joinColumns = @JoinColumn(name="booking_Id"),           //current name in db
            inverseJoinColumns =@JoinColumn(name="guest_Id")        //other side name in db

    )
    private Set<GuestEntity> guests;


}
