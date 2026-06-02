package ConzyNestapp.com.CozyNest.Entity;

import ConzyNestapp.com.CozyNest.Entity.Enums.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

////@Entity
//@Getter
//@Setter
//@Table(name="Payment")
public class PaymentEntity {            //no need as Booking also can take this Just need Transactional/PayemntSession Id.inside booking
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false,name="payment_Status")
    private Status paymentStatus;

    @Column(nullable = false,precision = 10,scale=2)
    private BigDecimal price;

    @OneToOne(fetch = FetchType.LAZY)
     //Initially it should be nullable
    private BookingEntity booking;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
