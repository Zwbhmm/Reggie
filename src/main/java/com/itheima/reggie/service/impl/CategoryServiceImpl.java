package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CategoryException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //查询有没有菜品和这个id的名字有关联
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //计数，看ID==id的有几个，如果大于0个说明有与它关联的，表示不能删除
        lambdaQueryWrapper.eq(Dish::getCategoryId, id);
        //将这个构造器添加进去
        int count1 = dishService.count(lambdaQueryWrapper);
        if(count1>0){
            //表示有关联，不能删除，抛出异常
            throw new CategoryException("和菜品有关联，不能删除！");
        }

        //查询有没有菜品和这个id的名字有关联
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        //计数，看ID==id的有几个，如果大于0个说明有与它关联的，表示不能删除
        lambdaQueryWrapper1.eq(Setmeal::getCategoryId, id);
        //将这个构造器添加进去
        int count2 = setmealService.count(lambdaQueryWrapper1);
        if(count2>0){
            //表示有关联，不能删除，报出异常
            throw new CategoryException("和套餐有关联，不能删除！");
        }
        //都没有关联，可以删除,调用父类的removeById
        super.removeById(id);
    }
}
