package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 保存菜品或者套餐
     * @param category
     * @return
     */
    @PostMapping()
    public R<Category> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success(category);
    }

    /**
     * 实现分类管理的首页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        //执行查询
        categoryService.page(pageInfo,lambdaQueryWrapper);
        return  R.success(pageInfo);
    }

    /**
     * 删除菜品分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除菜品{}",ids);
        categoryService.remove(ids);
        return  R.success("删除成功");
    }

    /**
     * 编辑菜品分类
     * @param category
     * @return
     */
    @PutMapping
    public R<String> edit(@RequestBody Category category){
        log.info("修改菜品：{}",category.getName());
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 菜品管理模块里面的新增菜品里面的菜品分类下拉框需要的数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        log.info("拉取菜品分类的数据...");
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());

        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lambdaQueryWrapper);

        return R.success(list);
    }

}
