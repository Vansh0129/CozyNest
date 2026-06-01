package ConzyNestapp.com.CozyNest.Security;

import ConzyNestapp.com.CozyNest.Dto.Request.LoginDto;
import ConzyNestapp.com.CozyNest.Dto.Request.SignUpDto;
import ConzyNestapp.com.CozyNest.Dto.Response.UserDto;
import ConzyNestapp.com.CozyNest.Entity.Enums.Role;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import ConzyNestapp.com.CozyNest.Repository.UserRepo;
import ConzyNestapp.com.CozyNest.Service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDto signUp(SignUpDto signUpDto) {            //1st time
        UserEntity user=userRepo.findByEmail(signUpDto.getEmail()).orElse(null);
        if(user!=null) throw new IllegalStateException("User already Exist !");
        user=modelMapper.map(signUpDto,UserEntity.class);
        user.setRoles(List.of(Role.GUEST));
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user=userRepo.save(user);
        return modelMapper.map(user, UserDto.class);

    }

    @Override
    public String[] login(LoginDto dto) {
        Authentication auth =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));  //isAuthenticated become true  and rerturn obj
        UserEntity user=(UserEntity) auth.getPrincipal()   ;        // have UserEntity feed from the Filter
        String[] tokens=new String[]{jwtService.generateToken(user),jwtService.moreSecuredToken(user)};

        return tokens;
    }

    @Override
    public String[] refresh(String refreshToken) {

            Long userId =jwtService.idFromToken(refreshToken);

            UserEntity user=userRepo.findById(userId).orElse(null);
            if(user==null){
                throw new ExpiredJwtException(null,null,"Refresh Token is Invalid");
            }
            String accessToken=jwtService.generateToken(user);
            return new String[]{accessToken,jwtService.moreSecuredToken(user)};


    }
    @Transactional
    public UserDto signUpForHotelManager(SignUpDto signUpDto) {            //admin
        UserEntity user=userRepo.findByEmail(signUpDto.getEmail()).orElse(null);
        if(user!=null) throw new IllegalStateException("User already Exist !");
        user=modelMapper.map(signUpDto,UserEntity.class);
        user.setRoles(List.of(Role.GUEST,Role.HOTEL_MANAGER));
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user=userRepo.save(user);
        return modelMapper.map(user, UserDto.class);

    }
}
