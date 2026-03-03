package com.example.minimall.service;

import com.example.minimall.entity.User;
import com.example.minimall.repository.UserRepository;
import com.example.minimall.util.ValidationUtil;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepo;
    public UserService(UserRepository userRepo) { this.userRepo = userRepo; }

    public User register(String username, String password) {
        // 用户名规则校验
        String msg = ValidationUtil.validateUsername(username);
        if (msg != null) throw new RuntimeException(msg);

        // 密码规则校验
        msg = ValidationUtil.validatePassword(username, password);
        if (msg != null) throw new RuntimeException(msg);

        if (userRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(password); // 简化：明文
        return userRepo.save(u);
    }

    public User login(String username, String password) {
        return userRepo.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }
}
