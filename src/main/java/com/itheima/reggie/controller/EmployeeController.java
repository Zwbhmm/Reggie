package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录方法
     * @param request
     * @param emp
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee emp){
        //获取用户输入的密码，将用户输入的密码按照MD5加密
        String password= emp.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //System.out.println(password);

        //查询数据库(MP)
        LambdaQueryWrapper<Employee> lambdaQueryWrapper =new LambdaQueryWrapper<Employee>();
        lambdaQueryWrapper.eq(Employee::getUsername,emp.getUsername());
        Employee one = employeeService.getOne(lambdaQueryWrapper);

        //查看数据库中有无这个账号
        if(one==null){
            //给出提示错误信息，用R类封装这个信息
            return R.error("账号不存在！");
        }
        //查看数据库中这个账号是否禁用
        if(one.getStatus()==0){
            //给出提示错误信息
            return R.error("账号已被禁用");
        }
        //对比密码是否正确
        if(!one.getPassword().equals(password)){
            //密码不正确
            return R.error("密码不正确，请重试！");
        }
        //存到Session
        request.getSession().setAttribute("employee",one.getId());
        return R.success(one);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //移除Session数据
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    /**
     * 新增员工方法
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());

        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
       //公共字段在每个表里都有，所以没必要每个表都单独加了，直接用MP提供的方式解决
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
       // Long empId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 登录界面之后的数据分页展示功能
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper= new LambdaQueryWrapper<>();
        //添加过滤条件
        lambdaQueryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(Employee::getUsername);
        //执行查询
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return  R.success(pageInfo);
    }

    /**
     * 更新和修改数据
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateUser(empId);
        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);
        return R.success("修改成功！");
    }

    /**
     * 根据Id查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("查询id为"+id+"的员工信息");
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            //查询成功
            return R.success(employee);
        }
        return R.error("查询失败");
    }
}
