package com.atguigu.gmall.product.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.minio")
//和配置文件绑定的
//自动把配置文件中 app.minio 下配置的每个属性全部和这个JavaBean的属性一一对应
@Component
@Data
public class MinioProperties {
    String endpoint;
    String ak;
    String sk;
    String bucketName;
}
