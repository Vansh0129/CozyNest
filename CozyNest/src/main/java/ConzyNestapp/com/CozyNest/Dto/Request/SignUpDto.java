package ConzyNestapp.com.CozyNest.Dto.Request;

import ConzyNestapp.com.CozyNest.Entity.Enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @NotNull(message = "Name must not be null !")
    private String  name;
    @Email(message = "Email cannot be null !")
    private String email;
    @Email(message = "Password cannot be null !")
    private String password;

}
