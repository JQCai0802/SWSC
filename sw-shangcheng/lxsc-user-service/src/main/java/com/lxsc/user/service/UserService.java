package com.lxsc.user.service;

import com.lxsc.user.model.Address;
import com.lxsc.user.model.User;

import java.util.List;

public interface UserService {
    int login(User user);

    int regUser(User user);

    List<Address> getUserAddressByUserId(Long userId);
}
