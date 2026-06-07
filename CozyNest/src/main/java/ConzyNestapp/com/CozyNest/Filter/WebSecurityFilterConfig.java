package ConzyNestapp.com.CozyNest.Filter;

import ConzyNestapp.com.CozyNest.Security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityFilterConfig {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    private final JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity

                .csrf(csrfConfig->csrfConfig.disable())
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
                //Api/v1 -> removed from dispatching servlet
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/admin/**").hasRole("HOTEL_MANAGER")  //only for hotel manager

                                .requestMatchers("/booking/**").authenticated()     //must authenticated
                                .anyRequest().permitAll()

                )
                .exceptionHandling(customExceptionHandling->customExceptionHandling.accessDeniedHandler(accessDeniedHandler()))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfig){
        return authenticationConfig.getAuthenticationManager();

    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){       //add the response to error and not to body .
        return ((request, response, accessDeniedException) ->
                handlerExceptionResolver.resolveException(request,response,null,accessDeniedException));

    }

}
