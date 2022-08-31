package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;

@RestController
public class ThreadPoolController {


    @Autowired
    ThreadPoolExecutor executor;


    @GetMapping("/close/pool")//    关闭线程池
    public Result closePool(){
        executor.shutdown();
        return Result.ok();
    }

@GetMapping("/monitor/pool")
    public Result monitorPool(){

    long taskCount = executor.getTaskCount();
    int corePoolSize = executor.getCorePoolSize();

    return Result.ok(corePoolSize+"======="+taskCount);
}

}
