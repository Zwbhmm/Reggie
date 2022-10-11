package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;
    /**
     *重写新增方法，保存到两个数据可里面
     * @param setmealDto
     */
    @Override
    public void saveWithDishes(SetmealDto setmealDto) {
        //1.保存setmeal 到Setmeal数据库表中
        this.save(setmealDto);
        Long id=setmealDto.getId();
        log.info(id.toString());
        //2.创建List<setmealDish>对象,读取setmealDto的getsetmealDishes,将setmealDto的id赋值给每个setmealDish
       List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
//        for(SetmealDish setmealDish: setmealDishes){
//            setmealDish.setDishId(setmealDto.getId());
//            setmealDishService.save(setmealDish);
//        }
        setmealDishes.stream().map((item)->{
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());
        log.info(setmealDishes.toString());
        //3.修改数据库
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 重写查询方法，从两个数据库里面读取东西
     * @param id
     */
    @Override
    public SetmealDto getByIdWithDishes(Long id) {
        //1.创建SetmealDto对象
        SetmealDto setmealDto = new SetmealDto();
        //2.先从数据库setmeal中查询Setmeal的数据
        Setmeal setmeal = this.getById(id);
        //2.1将数据copy给SetmealDto
        BeanUtils.copyProperties(setmeal,setmealDto);
        //3.再从数据库表setmealDish中查询除了CategoryName的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(id!=null,SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(lambdaQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
        //4.查询CategoryName
        Category category = categoryService.getById(setmeal.getCategoryId());
        setmealDto.setCategoryName(category.getName());

        return setmealDto;
    }

    /**
     *     //重写更新方法，从两个数据库表里更新东西
     * @param setmealDto
     */
    @Override
    public void updateWithDishes(SetmealDto setmealDto) {
        log.info(setmealDto.toString());
        //1.将setmealDto的部分数据保存到setmeal表中
        this.updateById(setmealDto);
        //2.将setmealDish表里关于这个套餐的数据删掉，重新添加
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
       // lambdaQueryWrapper.eq()
        //3.添加新的数据

    }

}
