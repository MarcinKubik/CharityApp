package pl.coderslab.charity.interfaces;

import pl.coderslab.charity.entity.User;

public interface UserServiceInterface {
    User findByEmail(String name);
    void save(User user);
}
