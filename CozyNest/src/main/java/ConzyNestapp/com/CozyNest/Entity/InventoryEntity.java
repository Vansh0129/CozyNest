package ConzyNestapp.com.CozyNest.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="Inventory",
        uniqueConstraints =@UniqueConstraint(
                name = "unique_hotel_room_date",
                columnNames = {"hotelId","roomId","date"}       //name of db saved entity
        )

)
@AllArgsConstructor @NoArgsConstructor @Builder     //fixed annotation if need to use Builder
public class InventoryEntity {              //useful such that in future if we need to check wheather we have rooms or not it will help ous
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="hotel_Id",nullable = false)
    private HotelEntity hotelId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="room_Id",nullable = false)
    private RoomEntity roomId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false,columnDefinition = "INTEGER DEFAULT 0")        //setting by default//like it is queary in hibernate (booked_count INTEGER DEFAULT 0 not null)
    private Integer bookedCount;           //like initially the count is zero so user can see full booking sloat available

    @Column(nullable = false)
    private Integer totalCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false,precision =5,scale=2)
    private BigDecimal surgeFactor;

    @Column(nullable = false,precision =10,scale=2)
    private BigDecimal price;       //baseprice*surgeFactor

    @Column(nullable = false)
    private String city;


    @Column(nullable = false)
    private Boolean  closed;

}
//Imp for availblity cheking if and only if the hotel is active