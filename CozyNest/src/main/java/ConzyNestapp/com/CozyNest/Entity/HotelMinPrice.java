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
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelMinPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="hotel_Id",nullable = false)
    private HotelEntity hotel;        //for the hotel

    @Column(nullable = false)
    private BigDecimal price;       //cheapest room of Day

    @Column(nullable = false)
    private LocalDate date;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public HotelMinPrice(HotelEntity hotel, LocalDate date) {
        this.hotel=hotel;
        this.date=date;
    }
}
