package ConzyNestapp.com.CozyNest.Service;

import ConzyNestapp.com.CozyNest.Dto.BookingDto;
import ConzyNestapp.com.CozyNest.Dto.UpdatedUserDto;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    UserEntity getUserById(Long id);

    UpdatedUserDto getUserData();

    void UpdateUser(UpdatedUserDto userDto);

    List<BookingDto> getAllBookingForUser();
}
