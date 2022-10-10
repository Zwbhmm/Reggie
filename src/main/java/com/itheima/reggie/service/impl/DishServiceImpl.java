package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.ConsumerException;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    /**
     * 在修改菜品功能里面，需要回显数据库中的数据，但回显的数据来自两个表，所以重写查询方法
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavors(Long id) {
        //1.查询Dish的数据
        Dish dish = this.getById(id);
        //2.在DishFlavor表中查询dish_id为id的flavor数据
        //2.1创建构造器
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2.2添加查询条件
        lambdaQueryWrapper.eq(id!=null,DishFlavor::getDishId,id);
        //2.3调用list方法查询
        List<DishFlavor> listFlavors = dishFlavorService.list(lambdaQueryWrapper);

        //创建DishDto对象，将dish复制给dishDto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //将listFlavors传给dishDto
        dishDto.setFlavors(listFlavors);
        return dishDto;
    }

    /**
     * 在修改菜品功能里面，需要同时修改两个表，在这里重写更新数据的方法
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavors(DishDto dishDto) {
        //1.更新dish表，直接用dishDto更新即可，dish有的字段dishDto全有
        this.updateById(dishDto);
        //2.获取dishFlavors，先删除相应的记录，在重新添加
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(dishDto.getId()!=null,DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        //2.1重新添加

        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        for (int i = 0; i < dishFlavors.size(); i++) {
            DishFlavor dishFlavor = dishFlavors.get(i);
            log.info(dishFlavor.toString());
            dishFlavor.setDishId(dishDto.getId());
            dishFlavorService.save(dishFlavor);
        }
    }

    /**
     * 重写方法，实现逻辑删除
     * @param ids
     */
    @Override
    @Transactional
    public void removeByIds(List<Long> ids) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ids!=null,Dish::getId,ids);
        List<Dish> list = this.list(lambdaQueryWrapper);
        for (Dish dish : list){
            Integer status = dish.getStatus();
            if(status==0){
                dish.setIsDeleted(1);
                this.removeById(dish);
            }else{
                throw new ConsumerException("当前状态为出售状态，不能删除");
            }
        }
    }

    /**
     *删除菜品功能里面，需要同时修改两个表，在这里重写删除数据的方法
     * @param ids
     */
    @Override
    public void deleteWithFlavors(List ids) {
        //1.删除Dish表里是ids的数据
        this.removeByIds(ids);
        //2.删除DishFlavors里面dish_id是ids的字段
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ids!=null,DishFlavor::getDishId,ids);
        List<DishFlavor> list = dishFlavorService.list(lambdaQueryWrapper);
    }
}
