package ConzyNestapp.com.CozyNest.Dto;

import ConzyNestapp.com.CozyNest.Entity.Enums.Gender;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class UpdatedUserDto {
    private String name;
    private String email;
    private Gender gender;
    @Past
    private LocalDate birthDate;

}
