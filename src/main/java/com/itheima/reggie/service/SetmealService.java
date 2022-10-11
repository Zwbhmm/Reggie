package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    //重写新增方法，保存到两个数据可里面
    void saveWithDishes(SetmealDto setmealDto);
    //重写查询方法，从两个数据库里面读取东西
    SetmealDto getByIdWithDishes(Long id);
    //重写更新方法，从两个数据库表里更新东西
    void updateWithDishes(SetmealDto SetmealDto);
}
