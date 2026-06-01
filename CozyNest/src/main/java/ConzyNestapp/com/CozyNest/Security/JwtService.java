package ConzyNestapp.com.CozyNest.Security;


import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.SecretKey}")
    private String jwtSecreteKey;

    private SecretKey getSecreteKey() {
        return Keys.hmacShaKeyFor(jwtSecreteKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserEntity user){
        return Jwts.builder()
                .subject(user.getId().toString())         //28-30 payload
                .claim("UserName",user.getUsername())     //values in the form of key-value
                .claim("Gmail",user.getEmail())
                .claim("Role",user.getRoles())     //Authorization part
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*4))  //1000 millisecond=1sec,1sec*60=1 min
                .signWith(getSecreteKey())                      // secrete sign
                .compact();                         //now concat and build with all details

    }   //Create Jwt tokens


    public String moreSecuredToken(UserEntity userDto){   //refresh token
        String refreshToken= Jwts
                .builder()
                .subject(userDto.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(getSecreteKey())
                .compact();
        return refreshToken;
    }
    public Long idFromToken(String token) throws JwtException {
        //we need claim it means payload part
        //but first need to verify SecreteKey
        //passing Signed UserToken
        //taking payload from it
        //Deriving the claims !
        try{
        Claims claims=Jwts.parser()
                .verifyWith(getSecreteKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());}
        catch (JwtException e){
            throw new JwtException("expired or invalid token ");            //not catched as this is not under DS it is in some other context!
        }



    }

}
