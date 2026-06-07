package ConzyNestapp.com.CozyNest.Dto.Response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    private Long id;
    @NotNull(message = "Name must not be null !")
    private String  name;
    @Email(message = "Email cannot be null !")
    private String email;



}
