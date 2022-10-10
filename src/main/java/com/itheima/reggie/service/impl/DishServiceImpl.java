package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增数据里面，保存数据到数据库的两张表
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到Dish表
        this.save(dishDto);

        //获取菜品的Id，因为dishDto里面只有category_id,在把菜品的基本信息保存到Dish表中，会生成这个菜的Id
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        //flavors.forEach(item->{item.setDishId(dishId);});
        flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        log.info(flavors.toString());
        dishFlavorService.saveBatch(flavors);

    }
}
