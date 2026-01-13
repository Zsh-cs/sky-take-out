package com.sky.service;

import com.sky.entity.Address;

import java.util.List;

public interface AddressBookService {
    // 新增地址
    void save(Address address);

    // 根据id查询地址
    Address getById(Long id);

    // 根据id修改地址
    void updateById(Address address);

    // 根据id删除地址
    void deleteById(Long id);

    // 查询当前用户的所有地址
    List<Address> list();

    // 设置默认地址：需要先清除原有默认地址
    void setDefault(Address address);

    // 查询默认地址
    Address getDefault();
}
