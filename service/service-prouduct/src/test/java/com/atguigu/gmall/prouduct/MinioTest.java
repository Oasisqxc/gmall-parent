package com.atguigu.gmall.prouduct;


import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;


//@SpringBootTest
public class MinioTest {

    @Test
    public void uploadFile() throws Exception {
        // 1.使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        try {
            MinioClient minioClient =
                    new MinioClient("http://192.168.200.100:9000",
                    "admin",
                    "admin123456");

            // 2.检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("gmall");
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                //3. 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket("gmall");
            }

            // 4.使用putObject上传一个文件到存储桶中。

//        文件流
            FileInputStream inputStream = new FileInputStream("E:\\8_尚品汇\\资料\\03 商品图片\\品牌\\pingguo.png");
//         文件上传参数
            PutObjectOptions options = new PutObjectOptions(inputStream.available(), -1L);
            options.setContentType("image/png");
            //       告诉minio这个文件上传的类型
            minioClient.putObject("gmall",
                    "pingguo.png",
                    inputStream,
                    options);
            System.out.println("上传成功");
        } catch (MinioException e) {
            e.printStackTrace();

        }
    }

}





