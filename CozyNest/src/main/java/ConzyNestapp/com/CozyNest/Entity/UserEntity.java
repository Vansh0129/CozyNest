package ConzyNestapp.com.CozyNest.Entity;

import ConzyNestapp.com.CozyNest.Entity.Enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="App_User")         //Db do not allow to create the table User ? then we need to rename it!
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    @Column(nullable = false)
    private String  name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> set=new HashSet<>();
        roles.forEach(role -> set.add(new SimpleGrantedAuthority("ROLE_"+role)));
        return set;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
