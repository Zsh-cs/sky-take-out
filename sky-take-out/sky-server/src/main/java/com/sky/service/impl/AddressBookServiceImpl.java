package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.constant.DefaultConstant;
import com.sky.context.BaseContext;
import com.sky.entity.Address;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    // 新增地址
    @Override
    public void save(Address address) {
        // 设置新增地址为非默认地址，否则数据库插入会报错，因为default字段有非空约束
        address.setIsDefault(DefaultConstant.NOT_DEFAULT);
        address.setUserId(BaseContext.getCurrentId());
        addressBookMapper.insert(address);
    }


    // 根据id查询地址
    @Override
    public Address getById(Long id) {
        Address address = addressBookMapper.selectById(id);
        return address;
    }


    // 根据id修改地址
    @Override
    public void updateById(Address address) {
        addressBookMapper.updateById(address);
    }


    // 根据id删除地址
    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }


    // 查询当前用户的所有地址
    @Override
    public List<Address> list() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Address> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Address::getUserId,userId);
        List<Address> list = addressBookMapper.selectList(lqw);
        return list;
    }


    // 设置默认地址：需要先清除原有默认地址
    @Transactional
    @Override
    public void setDefault(Address address) {

        // 先清除原有默认地址
        LambdaQueryWrapper<Address> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Address::getIsDefault,DefaultConstant.DEFAULT);
        Address defaultAddress = addressBookMapper.selectOne(lqw);
        defaultAddress.setIsDefault(DefaultConstant.NOT_DEFAULT);
        addressBookMapper.updateById(defaultAddress);

        // 再设置新的默认地址
        address.setIsDefault(DefaultConstant.DEFAULT);
        addressBookMapper.updateById(address);
    }


    // 查询默认地址
    @Override
    public Address getDefault() {
        LambdaQueryWrapper<Address> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Address::getIsDefault,DefaultConstant.DEFAULT);
        Address address = addressBookMapper.selectOne(lqw);
        return address;
    }


    // 将省名、市民、区名、详细地址拼接起来组成完全地址
    @Override
    public String getFullAddressById(Long id) {
        Address address = getById(id);
        String fullAddress=
                address.getProvinceName()+address.getCityName()+address.getDistrictName()+address.getDetail();
        return fullAddress;
    }
}


