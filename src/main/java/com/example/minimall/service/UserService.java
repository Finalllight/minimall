package com.example.minimall.service;
import com.example.minimall.entity.User;
import com.example.minimall.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepo;
    public UserService(UserRepository userRepo) { this.userRepo = userRepo; }

    public User register(String username, String password) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        return userRepo.save(u);
    }

    public User login(String username, String password) {
        return userRepo.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }
}
