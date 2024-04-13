package com.lxsc.user.service.impl;

import com.lxsc.user.mapper.AddressMapper;
import com.lxsc.user.mapper.UserMapper;
import com.lxsc.user.model.Address;
import com.lxsc.user.model.User;
import com.lxsc.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private AddressMapper addressMapper;

    @Override
    public int login(User user) {
        User dbUser=userMapper.selectUserByPhone(user.getPhone());
        //无此用户
        if(dbUser==null){
            return 1;
        }
        //有该用户但密码不对
        if(!dbUser.getPassword().equals(user.getPassword())){
            return 2;
        }
        //用前面对象的属性值填补后面对象的属性值
        BeanUtils.copyProperties(dbUser,user);
        return 0;
    }

    @Override
    public int regUser(User user) {
        try {
            userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            return 1;//排除重复异常，phone不重复就算注册成功
        }
        return 0;
    }

    @Override
    public List<Address> getUserAddressByUserId(Long userId) {
        return addressMapper.selectByUserId(userId);
    }
}
