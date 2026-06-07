package ConzyNestapp.com.CozyNest.Service.ServiceImpl;


import ConzyNestapp.com.CozyNest.Dto.BookingDto;
import ConzyNestapp.com.CozyNest.Dto.UpdatedUserDto;
import ConzyNestapp.com.CozyNest.Entity.BookingEntity;
import ConzyNestapp.com.CozyNest.Entity.Enums.BookingStatus;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import ConzyNestapp.com.CozyNest.Exception.ResourceNotFoundException;
import ConzyNestapp.com.CozyNest.Repository.BookingRepository;
import ConzyNestapp.com.CozyNest.Repository.UserRepo;
import ConzyNestapp.com.CozyNest.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static ConzyNestapp.com.CozyNest.Utils.AppUtils.getUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserEntity getUserById(Long id) {
        return   userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Cannot find the User With Id "+id));

    }

    @Override
    public UpdatedUserDto getUserData() {
        return modelMapper.map(userRepo.findByEmail(getUser().getEmail()), UpdatedUserDto.class);
    }

    @Override
    public void UpdateUser(UpdatedUserDto userDto) {
       UserEntity user= userRepo.findByEmail(getUser().getEmail()).orElseThrow(()->new UsernameNotFoundException("Invalid Credentials"));
       modelMapper.map(userDto,user);
       user.setId(getUser().getId());
       userRepo.save(user);
       return ;
    }

    @Override
    public List<BookingDto> getAllBookingForUser() {
        UserEntity user=getUser();
         user= userRepo.findByEmail(user.getEmail()).orElseThrow(()->new UsernameNotFoundException("Invalid Credentials"));
        List<BookingEntity> bookings=bookingRepository.findAllBookingForUser(user, BookingStatus.CONFIRMED);
        return bookings.stream()
                .map(booking->modelMapper.map(booking,BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  userRepo.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Cannot find username "+username));

    }
}
