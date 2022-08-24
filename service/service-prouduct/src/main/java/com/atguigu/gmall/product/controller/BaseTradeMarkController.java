package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
public class BaseTradeMarkController {
    @Autowired
    BaseTrademarkService baseTrademarkService;

    //    admin/product/baseTrademark/1/10
//    分页查询品牌数据
    @GetMapping("/baseTrademark/{pNumber}/{size}")
    public Result getBaseTrademark(@PathVariable("pNumber") Long pNumber,
                                   @PathVariable("size") Long size
    ) {

        Page<BaseTrademark> page = new Page<>(pNumber, size);

        //分页查询（分页信息、查询到的记录集合）
        Page<BaseTrademark> pageResult = baseTrademarkService.page(page);

        return Result.ok(pageResult);
    }

    //    admin/product/baseTrademark/get/2
//    根据id查询品牌
    @GetMapping("/baseTrademark/get/{id}")
    public Result getBaseTrademark(@PathVariable("id") Long id) {

        BaseTrademark trademark = baseTrademarkService.getById(id);
        return Result.ok(trademark);

    }

    //    根据id修改品牌
//    baseTrademark/update
    @PutMapping("/baseTrademark/update")
    public Result updateTrademark(@RequestBody BaseTrademark baseTrademark) {

        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    //    添加品牌
//    baseTrademark/save
    @PostMapping("/baseTrademark/save")
    public Result saveTrademark(@RequestBody BaseTrademark trademark) {

        baseTrademarkService.save(trademark);
        return Result.ok();
    }

    //    删除品牌
//    baseTrademark/remove/{id}
    @DeleteMapping("/baseTrademark/remove/{tid}")
    public Result deleteTrademark(@PathVariable("tid") Long tid) {

        baseTrademarkService.removeById(tid);
        return Result.ok();

    }
}
