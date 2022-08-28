package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类有关的API
 * 以后所有的远程调用都是内部接口：命名规范：/api/inner/rpc/模块名/路径
 */
@Api(tags = "远程调用三级分类RPC接口")
@RestController
@RequestMapping("/api/inner/rpc/product")
public class CategoryApiController {

    @Autowired
    BaseCategory2Service baseCategory2Service;

    //    查询出所有分类，并封装成树形菜单结构
    @ApiOperation("三级分类树形结构查询")
    @GetMapping("/category/tree")
    public Result getAllCategoryWithTree() {

        List<CategoryTreeTo> categoryTreeTos = baseCategory2Service.getAllCategoryWithTree();
        return Result.ok(categoryTreeTos);
    }

}
