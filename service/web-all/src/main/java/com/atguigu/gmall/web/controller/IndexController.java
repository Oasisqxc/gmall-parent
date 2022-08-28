package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.web.feign.CategoryFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Api(tags = "首页分类与商品详情")
@Controller
public class IndexController {

    @Autowired
    CategoryFeignClient categoryFeignClient;

    @ApiOperation("跳转到首页")
    @GetMapping({"/", "index"})
    public String indexPage(Model model) {
//远程查询出所有菜单，封装成一个树形结构的模型
        Result<List<CategoryTreeTo>> result = categoryFeignClient.getAllCategoryWithTree();

        if (result.isOk()) {

            List<CategoryTreeTo> data = result.getData();
            model.addAttribute("list", data);
        }
        return "index/index";//页面的逻辑视图名
    }
}
