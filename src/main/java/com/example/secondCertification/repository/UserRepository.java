package com.example.secondCertification.repository;

import com.example.secondCertification.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByPhone(String phone);
    User findUserByLogin(String login);
    User findUserByLoginAndPassword(String login, String password);
    User findUserByToken(String token);
}
