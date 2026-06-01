package ConzyNestapp.com.CozyNest.Service;


import ConzyNestapp.com.CozyNest.Dto.Request.LoginDto;
import ConzyNestapp.com.CozyNest.Dto.Request.SignUpDto;
import ConzyNestapp.com.CozyNest.Dto.Response.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService  {
    UserDto signUp(SignUpDto signUpDto);
    String[] login(LoginDto dto);


    String[] refresh(String refreshToken);
}
