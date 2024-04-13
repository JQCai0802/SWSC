package com.lxsc.user.mapper;

import com.lxsc.user.model.Address;

import java.util.List;

public interface AddressMapper {
    int deleteByPrimaryKey(Long id);
    //传入完整数据的对象
    int insert(Address record);
    //传入不全数据的对象
    int insertSelective(Address record);

    Address selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    List<Address> selectByUserId(Long userId);
}