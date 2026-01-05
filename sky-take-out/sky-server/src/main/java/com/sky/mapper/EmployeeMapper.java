package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.SqlOperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

//Todo: 可以考虑使用MyBatisPlus简化代码
@Mapper
public interface EmployeeMapper {

    // 根据用户名查询员工
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    // 新增用户，自动填充公共字段
    @AutoFill(SqlOperationType.INSERT)
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, create_user, update_time,  update_user) " +
            "values " +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{createTime}, #{createUser}, #{updateTime}, #{updateUser})")
    void save(Employee employee);

    // 员工分页查询
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    // 为了修改的通用性，我们使用动态SQL编写更加通用的update方法：根据主键动态修改员工
    // 自动填充公共字段
    // Caution: 约定好如果要使用@AutoFill注解，那么实体对象必须作为方法的第一个入参
    @AutoFill(SqlOperationType.UPDATE)
    void update(Employee employee);

    // 根据id查询员工信息
    @Select("select id,username,name,phone,sex,id_number from employee where id=#{id}")
    Employee getById(Long id);

}
