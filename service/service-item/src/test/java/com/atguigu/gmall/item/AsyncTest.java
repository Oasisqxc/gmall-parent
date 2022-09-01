package com.atguigu.gmall.item;



import java.util.concurrent.*;

public class AsyncTest {

   static ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws Exception {
//        多任务组合
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println("aaa");
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            return 222;
        });


        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
            System.out.println("bbb");
        });

//        CompletableFuture.allOf(future1,future2,future3).thenRun(()->{
//            System.out.println("都完了========");
//        });

        CompletableFuture<Void> all = CompletableFuture.allOf(future1, future2, future3);

        all.get();

        System.out.println("hhhh");

        Thread.sleep(10000L);
    }
    public static void thenXXX(String[] args) throws InterruptedException {
//        先计算1+1 算出结果再 执行+1
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println(Thread.currentThread().getName() + ":计算1+1");
//            return 2;
//        }, executor);
//
//       future.thenRunAsync(()->{
//           System.out.println(Thread.currentThread().getName()+"呵呵");
//       },executor);

//        future.thenAcceptAsync((t)->{
//            System.out.println(Thread.currentThread().getName()+"接到的t是"+t);
//            System.out.println(t+3);
//        },executor);

//        future.thenApplyAsync((t)->{
//            System.out.println(Thread.currentThread().getName()+"接到的t是"+t);
//            return t+2;
//        },executor);

        CompletableFuture.supplyAsync(()->{
            return 5;
        }).thenApplyAsync((t)->{
            return t+3;
        }).thenApply((t)->{
            return t*5;
        }).thenAcceptAsync((t)->{
            System.out.println("把"+t+"保存到数据库");
        }).whenComplete((t ,u)->{
            if (u!=null){
//                记录日志
            }
            System.out.println("执行结束，保存日志");
        });


        Thread.sleep(10000L);
    }

    public static void starAsync(String[] args) throws Exception {
        /*
        *   //1、启动异步任务
        // runAsync();
        //    1）、CompletableFuture.runAsync(Runnable) 返回 CompletableFuture<Void>;
        //             使用默认线程池(ForkJoinPool)，启动一个Runnable任务进行执行，没有返回结果
        //    2）、CompletableFuture.runAsync(Runnable,指定线程池) 返回 CompletableFuture<Void>;
        //             使用指定线程池，启动一个Runnable任务进行执行，没有返回结果
        // supplyAsync();
        //    1）、CompletableFuture.supplyAsync(Runnable) 返回 CompletableFuture<Integer>
        //             使用默认线程池(ForkJoinPool)，启动一个Runnable任务进行执行，有返回结果
        //    2）、CompletableFuture.supplyAsync(Runnable,指定线程池)
        //             使用指定线程池，启动一个Runnable任务进行执行，有返回结果
        * */

        System.out.println(Thread.currentThread().getName()+":主线程开始");

//        CompletableFuture.runAsync(()->{
//            System.out.println(Thread.currentThread().getName() + ":" + UUID.randomUUID().toString());
//            System.out.println("aa");
//        },executor);


        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "呵呵");
            return 2;
        },executor);

        Integer integer = future.get();//阻塞等待异步结果
        Integer integer1 = future.get(1, TimeUnit.SECONDS);//限时等待

        System.out.println("异步结果"+integer);
        System.out.println("异步结果"+integer1);

        Thread.sleep(10000L);
    }



    public static void jieshao(String[] args) throws InterruptedException {

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


