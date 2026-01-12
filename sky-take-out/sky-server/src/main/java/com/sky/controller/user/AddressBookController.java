package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.Address;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端-地址簿接口
 * Caution: 地址簿是和用户强绑定的，每个用户有自己的地址簿
 */
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "用户端-地址簿接口")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    // 新增地址
    @ApiOperation("新增地址")
    @PostMapping
    public Result save(@RequestBody Address address) {
        log.info("新增地址：{}",address);
        addressBookService.save(address);
        return Result.success();
    }


    // 根据id查询地址
    @ApiOperation("根据id查询地址")
    @GetMapping("/{id}")
    public Result<Address> getById(@PathVariable Long id) {
        log.info("查询id={}的地址",id);
        Address address = addressBookService.getById(id);
        return Result.success(address);
    }


    // 根据id修改地址
    @ApiOperation("根据id修改地址")
    @PutMapping
    public Result updateById(@RequestBody Address address) {
        log.info("修改id={}地址为：{}", address.getId(),address);
        addressBookService.updateById(address);
        return Result.success();
    }


    // 根据id删除地址
    @ApiOperation("根据id删除地址")
    @DeleteMapping
    public Result delete(@RequestParam Long id) {
        log.info("删除id={}的地址", id);
        addressBookService.deleteById(id);
        return Result.success();
    }


    // 查询当前用户的所有地址
    @ApiOperation("查询当前用户的所有地址")
    @GetMapping("/list")
    public Result<List<Address>> list() {
        log.info("查询当前用户的所有地址");
        List<Address> list = addressBookService.list();
        return Result.success(list);
    }


    // 设置默认地址
    @ApiOperation("设置默认地址")
    @PutMapping("/default")
    public Result setDefault(@RequestBody Address address) {
        log.info("设置默认地址为：{}", address);
        addressBookService.setDefault(address);
        return Result.success();
    }


    // 查询默认地址
    @ApiOperation("查询默认地址")
    @GetMapping("/default")
    public Result<Address> getDefault() {
        log.info("查询默认地址");
        Address address = addressBookService.getDefault();
        return Result.success(address);
    }

}
