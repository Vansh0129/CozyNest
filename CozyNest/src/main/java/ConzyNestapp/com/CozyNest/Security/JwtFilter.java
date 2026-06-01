package ConzyNestapp.com.CozyNest.Security;

import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import ConzyNestapp.com.CozyNest.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;



@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeadertoken =request.getHeader("Authorization");
        //gives requestHeadertoken but, it presents in the form of "Bearer TokenString"
        System.out.println(requestHeadertoken);
        if(requestHeadertoken ==null ||!requestHeadertoken.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        String token=requestHeadertoken.split("Bearer")[1].trim();   //give actual JSON Web Token
//        String token = requestHeadertoken.substring(7);
        Long id=jwtService.idFromToken(token);      //extracted id
        if (id!=null && SecurityContextHolder.getContext().getAuthentication()==null){      //user found!
            UserEntity user=userService.getUserById(id);  //user fromDB
//
            UsernamePasswordAuthenticationToken authenticatedUser=
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

            authenticatedUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //Passing security context in auth


            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

        }filterChain.doFilter(request,response);  //set filterChange and move to next filter!
        return;
    }
}
