package ConzyNestapp.com.CozyNest.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Hotel")
public class HotelEntity {                   //HotelEntity->(KababCase)->hotel_entity  //DB internally
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;                //by default varchar
    @Column(columnDefinition = "TEXT[]")            //to say DB it is text array else it will give error because jpa unable to tell them
    private String[] photo;                         //Url

    @Column(columnDefinition = "TEXT[]")
    private String[] amenities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean active=false;

    @Embedded
    private HotelContactInfo contactInfo;

    @OneToMany(mappedBy = "hotelEntity",cascade = CascadeType.DETACH)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<RoomEntity> rooms;

    private String Owner;



}

//internally by using Embedded and Embeddable it adds
//contactInfo_adress
//contactInfo_email

