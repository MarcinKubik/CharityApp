package pl.coderslab.charity.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.entity.Role;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.interfaces.UserServiceInterface;
import pl.coderslab.charity.repository.RoleRepository;
import pl.coderslab.charity.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Optional<User> get(Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser;
    }

    @Override
    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    public void createFirstAdmin(){
        User admin = new User();
        admin.setName("Marcin");
        admin.setSurname("Kubik");
        admin.setEnabled(1);
        admin.setEmail("aaa@gmail.com");
        admin.setPassword(passwordEncoder.encode("haslo111"));
        admin.setPassword2("haslo111");
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        admin.setRoles(new HashSet<Role>(Arrays.asList(adminRole)));
        userRepository.save(admin);
    }

    public void update(User user){
        userRepository.save(user);
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}