package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;
    /**
     * 新增操作
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDishes(setmealDto);
        return  R.success("新增成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //1.构造page构造器，查询数据
        Page<Setmeal> setmealPage= new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        lambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, lambdaQueryWrapper);
        log.info(setmealPage.toString());
        //2.由于页面展示的数据有套餐分类名称这一项，但是setMeal表里面没有这一项，所以创建setmealDto对象，
        //将除了records之外的所有数据都转移到setmealDto上面来
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage);

        //2.1将所有的records里面的category_name都添加上名字
        //2.2根据setmealPage创建records,赋值完成后，传给setmealDtoPage
        List<Setmeal> setmealList = setmealPage.getRecords();
        List<SetmealDto> setmealDtoList = new ArrayList<>();
        for(Setmeal setmeal:setmealList){
            //创建一个setmealDto对象 暂时存放每个records，并将除了CategoryName这一项除外的所有项，从setmeal复制过来
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
             //从Category表里查询Category_Name
            Long categoryId = setmeal.getCategoryId();
            Category category = categoryService.getById(categoryId);
            //添加CategoryName到setmealDto
            setmealDto.setCategoryName(category.getName());
            //最后将所有的setmealDto对象都存到List集合中
            setmealDtoList.add(setmealDto);
        }
        //setmealDtoPage添加records这一项，即List<SetmealDto>
        setmealDtoPage.setRecords(setmealDtoList);
        return R.success(setmealDtoPage);
    }

    /**
     * 修改套餐里面的回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> edit(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByIdWithDishes(id);
        return R.success(setmealDto);
    }

    /**
     * 修改之后保存数据
     * @return
     */
    @PutMapping
    public R<String> editSave(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDishes(setmealDto);
       return null;
    }
}
