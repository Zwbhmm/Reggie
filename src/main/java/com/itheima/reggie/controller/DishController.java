package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增数据
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }

    /**
     * 数据分页展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
         //创建分页构造器
        Page<Dish>  pageInfo = new Page<>(page,pageSize);

        //创建page<DishDto>,因为实体类Dish里面没有category_Name;
        Page<DishDto> dishDtoPage = new Page<>();
        //创建条件查询器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper =new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name!=null,Dish::getName,name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,dishLambdaQueryWrapper);
        //pageInfo里面现在存有前端需要的数据，但是没有categoryName,将其拷贝给dishDtoPage，除了records，因为要对它处理，将categoryName加进去
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        //遍历pageInfo的records，设置一个新的dishDtoPage的records 对象，这个对象包括每个pageInfo的records和categoryName
        List<Dish> pageInfoRecords = pageInfo.getRecords();
        List<DishDto> dishDtos= pageInfoRecords.stream().map((item)->{
            DishDto dishDto = new DishDto();
            //将item的所有的属性copy到dishDto上面
            BeanUtils.copyProperties(item,dishDto);
            //通过item的category_id查询到dishDto的CategoryName
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDto.setCategoryName(category.getName());

            return dishDto;
        }).collect(Collectors.toList());

        //dishDtoPage 加入带有新的records的数据
        dishDtoPage.setRecords(dishDtos);

        return R.success(dishDtoPage);
    }

}
