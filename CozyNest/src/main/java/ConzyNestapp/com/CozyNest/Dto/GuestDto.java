package ConzyNestapp.com.CozyNest.Dto;

import ConzyNestapp.com.CozyNest.Entity.Enums.Gender;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class GuestDto {
    private Long id;
    private UserEntity userId;
    private String name;
    private Gender gender;
    private Long age;

    private LocalDateTime createdAt;

}
