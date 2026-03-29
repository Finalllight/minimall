package com.example.minimall.service;

import com.example.minimall.entity.User;
import com.example.minimall.repository.UserRepository;
import com.example.minimall.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
        u.setPassword(password);
        u.setRole("USER");// 简化：明文
        u.setBalance(new BigDecimal("88888.00"));
        return userRepo.save(u);
    }

    public User login(String username, String password) {
        return userRepo.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }
    public List<User> listAll() { return userRepo.findAll(); }

    public User getById(Long id) {
        return userRepo.findById(id).orElse(null);
    }
    public void updateBalance(Long userId, BigDecimal balance) {
        userRepo.findById(userId).ifPresent(u -> {
            u.setBalance(balance);
            userRepo.save(u);
        });
    }
}
