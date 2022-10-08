package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    //重写removeById方法，如果菜品或者套餐有和Category有关联的话不能删除
    void remove(Long id);
}
