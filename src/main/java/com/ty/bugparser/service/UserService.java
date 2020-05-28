package com.ty.bugparser.service;

import com.ty.bugparser.pojo.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User getUserByUsernameAndPassword(String username, String password);

    User getUserByUsername(String username);

    int updateUser(User user);

}
