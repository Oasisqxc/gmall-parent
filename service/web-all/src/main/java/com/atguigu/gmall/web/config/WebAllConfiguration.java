package com.atguigu.gmall.web.config;

import com.atguigu.gmall.common.constant.SysRedisConst;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class WebAllConfiguration {
//    把用户id带到feign即将发起的请求中
    @Bean
    public RequestInterceptor userHeaderInterceptor(){

        return (template)->{
//            修改请求模板
//            随时调用，获取老请求
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = attributes.getRequest();
            String userId = request.getHeader(SysRedisConst.USERID_HEADER);

//            将用户id头添加到feign的新请求中
            template.header(SysRedisConst.USERID_HEADER,userId);

//            临时id也透传
           String tempId = request.getHeader(SysRedisConst.USERTEMPID_HEADER);
           template.header(SysRedisConst.USERTEMPID_HEADER,tempId);
        };
    }
}
