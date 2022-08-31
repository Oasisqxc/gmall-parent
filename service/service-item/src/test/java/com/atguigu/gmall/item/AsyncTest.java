package com.atguigu.gmall.item;



import java.util.concurrent.CompletableFuture;

public class AsyncTest {


    public static void main(String[] args) throws InterruptedException {

        System.out.println(Thread.currentThread().getName() + "：呵呵");
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ":aaaa");
        });
        System.out.println(Thread.currentThread().getName() + "：bbbb");
//t结果 u异常
        future.whenComplete((t, u)->{
            System.out.println("yyyyyy");
        });



        Thread.sleep(10000000L);
    }







    }


