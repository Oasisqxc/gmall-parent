package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.starter.cache.annotation.GmallCache;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.mapper.BaseCategory2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author hp
* @description 针对表【base_category2(二级分类表)】的数据库操作Service实现
* @createDate 2022-08-23 23:48:23
*/

@Service
public class BaseCategory2ServiceImpl extends ServiceImpl<BaseCategory2Mapper, BaseCategory2>
    implements BaseCategory2Service {

    @Resource
    BaseCategory2Mapper baseCategory2Mapper;

    //    根据一级分类id查询出所有二级分类
    @Override
    public List<BaseCategory2> getCategory1Child(Long c1Id) {
        QueryWrapper<BaseCategory2> wrapper = new QueryWrapper<>();
        wrapper.eq("category1_id", c1Id);
        List<BaseCategory2> baseCategory2List = baseCategory2Mapper.selectList(wrapper);
        return baseCategory2List;
    }

    @GmallCache(cacheKey = SysRedisConst.CACHE_CATEGORYS) //categorys
    @Override
    public List<CategoryTreeTo> getAllCategoryWithTree() {


        return baseCategory2Mapper.getAllCategoryWithTree();
    }

}




