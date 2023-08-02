package com.example.auditlec.service;

import com.example.auditlec.model.User;
import com.example.auditlec.model.dto.UserDto;
import com.example.auditlec.model.dto.UserHistoryResponseDto;

import java.util.List;

public interface UserService {
    User saveUser(UserDto user);

    User updateUser(Long id, UserDto user);

    void deleteUser(Long id);

    List<User> getAllUsers();

    User getUserById(Long id);

    List<UserHistoryResponseDto> getUserHistory(Long id);
}
