package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        //1.创建Setmeal对象，将setmealDto复制到Setmeal
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        //1.2保存setmeal到数据库
        setmealService.save(setmeal);
        //2.将setmealDto里面的setmealDishes，封装成一个list集合，然后保存到数据库setmeal_dish中
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        log.info(setmealDishes.toString());
        //2.1查询数据库，给每个setmealDish赋值：setmealId
        for(SetmealDish setmealDish : setmealDishes){

        }


        return  null;
    }
}
