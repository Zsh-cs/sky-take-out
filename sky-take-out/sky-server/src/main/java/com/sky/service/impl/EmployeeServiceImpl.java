package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordModifyDTO;
import com.sky.dto.page.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    // 员工登录
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        // 1.根据用户名查询数据库中的数据：使用了MyBatis-Plus提供的方法
        LambdaQueryWrapper<Employee> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,username);
        Employee employee = employeeMapper.selectOne(lqw);

        // 2.处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对：对前端传过来的明文密码进行MD5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        if (employee.getStatus() == StatusConstant.DISABLE) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 3.返回实体对象
        return employee;
    }


    // 新增员工
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();

        // 使用对象属性拷贝，将DTO数据拷贝到实体类中
        BeanUtils.copyProperties(employeeDTO,employee);

        // 设置实体类的其他属性，密码默认是123456，需要进行MD5加密
        //Caution: 实体类的公共字段已经统一进行自动填充，此处不必额外赋值
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        // 调用持久层方法新增员工
        employeeMapper.save(employee);
    }


    // 修改密码
    @Override
    public void modifyPassword(PasswordModifyDTO passwordModifyDTO) {
        Long empId = passwordModifyDTO.getEmpId();
        String oldPassword = DigestUtils.md5DigestAsHex(passwordModifyDTO.getOldPassword().getBytes());
        String newPassword = DigestUtils.md5DigestAsHex(passwordModifyDTO.getNewPassword().getBytes());

        Employee employee = employeeMapper.selectById(empId);
        // 校验旧密码的正确性
        String passwordInDB = employee.getPassword();
        if(!oldPassword.equals(passwordInDB)){
            throw new PasswordErrorException(MessageConstant.OLD_PASSWORD_ERROR);
        }

        // 如果旧密码正确，才允许修改密码
        employee.setPassword(newPassword);
        employeeMapper.update(employee);

    }


    // 员工分页查询
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {

        // 使用MP进行分页查询，弃用PageHelper，MP会自动过滤已被逻辑删除的记录
        Page<Employee> page=new Page<>(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        LambdaQueryWrapper<Employee> lqw=new LambdaQueryWrapper<>();

        String name=employeePageQueryDTO.getName();
        if(name!=null && !name.isEmpty()){
            lqw.like(Employee::getName,name);
        }
        lqw.orderByDesc(Employee::getCreateTime);

        employeeMapper.selectPage(page, lqw);
        return new PageResult(page.getTotal(),page.getRecords());
    }


    // 启用禁用员工账号
    @Override
    public void changeStatus(Integer status, Long id) {
        // 为了修改的通用性，我们不在持久层中单独编写changeStatus方法，而是使用动态SQL编写更加通用的update方法
        Employee employee=new Employee();
        employee.setStatus(status);
        employee.setId(id);
        employeeMapper.update(employee);
    }


    // 根据id查询员工信息
    @Override
    public EmployeeDTO getById(Long id) {
        EmployeeDTO employeeDTO=new EmployeeDTO();

        // 使用了MyBatis-Plus提供的方法
        Employee employee = employeeMapper.selectById(id);

        // 使用对象属性拷贝，将实体类数据拷贝到DTO中
        BeanUtils.copyProperties(employee,employeeDTO);
        return employeeDTO;
    }


    // 修改员工信息
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();

        // 使用对象属性拷贝，将DTO数据拷贝到实体类中
        // 实体类的公共字段已经统一进行自动填充，此处不必额外赋值
        BeanUtils.copyProperties(employeeDTO,employee);

        employeeMapper.update(employee);
    }

}
