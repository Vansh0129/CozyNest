package ConzyNestapp.com.CozyNest.Controller;

import ConzyNestapp.com.CozyNest.Dto.BookingDto;
import ConzyNestapp.com.CozyNest.Dto.UpdatedUserDto;
import ConzyNestapp.com.CozyNest.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Component
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity getUser(){           //new//Tested
        UpdatedUserDto updateUserDto=userService.getUserData();
        return ResponseEntity.ok(updateUserDto);
    }

    @PutMapping("/profile")
    public ResponseEntity updateUserData(@RequestBody UpdatedUserDto userDto){           //new//Tested
        userService.UpdateUser(userDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingDto>> findAllBookings(){           //new//Tested
        List<BookingDto> bookings=userService.getAllBookingForUser();
        return ResponseEntity.ok(bookings);
    }

}
