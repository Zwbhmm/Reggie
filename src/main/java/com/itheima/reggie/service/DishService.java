package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;

import java.util.List;

public interface DishService extends IService<Dish> {
    //在新增功能中，要一次性操作两个表，重写保存数据库的方法
    void saveWithFlavor(DishDto dishDto);
    //在修改菜品功能里面，需要回显数据库中的数据，但回显的数据来自两个表，所以重写查询方法
    DishDto getByIdWithFlavors(Long id);
    //在修改菜品功能里面，需要同时修改两个表，在这里重写更新数据的方法
    void updateWithFlavors(DishDto dishDto);
    //删除菜品功能里面，需要同时修改两个表，在这里重写删除数据的方法
    void deleteWithFlavors(List<Long> ids);
    //重写removeByIds方法，因为他是逻辑删除
    void removeByIds(List<Long> ids);
}
