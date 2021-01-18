package pl.coderslab.charity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.interfaces.UserServiceInterface;

import java.util.HashSet;
import java.util.Set;

public class SpringDataUserDetailsService implements UserDetailsService {

    private UserServiceInterface userServiceInterface;

    @Autowired
    public void setUserRepository(UserServiceInterface userServiceInterface){
        this.userServiceInterface = userServiceInterface;
    }

    @Override
    public UserDetails loadUserByUsername(String email){
        User user = userServiceInterface.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException(email);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getRoles().forEach(r ->
                grantedAuthorities.add(new SimpleGrantedAuthority(r.getName())));
        return new CurrentUser(user.getEmail(), user.getPassword(),
                grantedAuthorities, user);
    }
}
