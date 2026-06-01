package ConzyNestapp.com.CozyNest.Controller;

import ConzyNestapp.com.CozyNest.Dto.Request.LoginDto;
import ConzyNestapp.com.CozyNest.Dto.Request.SignUpDto;
import ConzyNestapp.com.CozyNest.Dto.Response.UserDto;
import ConzyNestapp.com.CozyNest.Security.AuthServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value="/auth")
@EnableMethodSecurity(securedEnabled=true)
public class AuthController {
    private final AuthServiceImpl authService;

    @PreAuthorize("hasRole('HOTEL_MANAGER')")
    @PostMapping("/signup/admin")
    public ResponseEntity<UserDto> signUpForAdmin(@RequestBody SignUpDto dto){
        UserDto userDto=authService.signUpForHotelManager(dto);
        return new ResponseEntity<>(userDto, HttpStatusCode.valueOf(201));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto dto){
        UserDto userDto=authService.signUp(dto);
        return new ResponseEntity<>(userDto, HttpStatusCode.valueOf(201));
    }

    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody LoginDto dto, HttpServletResponse response){
        String[] tokens=authService.login(dto);
        Cookie cookie=new Cookie("RefreshToken",tokens[1]);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return  ResponseEntity.ok(Map.of("AccessToken", tokens[0]));
    }

    @GetMapping("/refresh")
    public ResponseEntity<Map> refresh( HttpServletRequest request){
        if(request==null) throw new RuntimeException("signup Required !");
        String refreshToken= Arrays.stream(request.getCookies()).
                filter(cookie ->"RefreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()->new AuthenticationServiceException("Cannot found the RefreshToken from cookie"));
        String[] tokens=authService.refresh(refreshToken);
        return new ResponseEntity(Map.of("AccessToken",tokens[0]), HttpStatusCode.valueOf(201));
    }

}
