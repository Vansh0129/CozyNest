package ConzyNestapp.com.CozyNest.Service;

import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {
    UserEntity getUserById(Long id);
}
