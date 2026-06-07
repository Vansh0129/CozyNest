package ConzyNestapp.com.CozyNest.Utils;

import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtils {
     public static UserEntity getUser(){
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
