package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;

public interface DishService extends IService<Dish> {
    //在新增功能中，要一次性操作两个表，重写保存数据库的方法
    void saveWithFlavor(DishDto dishDto);
}
