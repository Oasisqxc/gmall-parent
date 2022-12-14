package com.atguigu.gmall.common.config.threadpool;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
//1、AppThreadPoolProperties 里面的所有属性和指定配置绑定
//2、AppThreadPoolProperties 组件自动放到容器中
//开启自动化属性绑定配置
@EnableConfigurationProperties(AppThreadPoolProperties.class)
@Configuration
public class AppThreadPoolAutoConfiguration {

    @Resource
    AppThreadPoolProperties threadPoolProperties;

    @Value("${spring.application.name}")
    String applicationName;
    @Bean
    public ThreadPoolExecutor coreExcutor() {
/*
* int corePoolSize, 核心线程池，cpu数
  int maximumPoolSize, 最大线程数
  long keepAliveTime, 线程存活时间
  TimeUnit unit,时间单位
  BlockingQueue<Runnable> workQueue, 阻塞队列数：大小需要合理
  ThreadFactory threadFactory, 线程工厂，自定义创建线程的方法
  RejectedExecutionHandler handler 拒绝策略
*
*
* */


        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                threadPoolProperties.getCore(),
                threadPoolProperties.getMax(),
                threadPoolProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(threadPoolProperties.getQueueSize()),//队列的大小由项目最终能占的最大内存决定
                new ThreadFactory() {//负责给线程池创建线程
                    int i=0;//记录线程自增id
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
           thread.setName(applicationName+"[core-thread-"+ i++ +"]");
                        return thread;
                    }
                },
                //生产环境用 CallerRuns，保证就算线程池满了，
                // 不能提交的任务，由当前线程自己以同步的方式执行
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
//每个线程的线程名都是默认的。
        return executor;
    }
}
