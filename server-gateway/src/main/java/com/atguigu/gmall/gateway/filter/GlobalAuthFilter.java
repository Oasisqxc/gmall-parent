package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.HttpClientUtil;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.gateway.properties.AuthUrlProperties;
import com.atguigu.gmall.model.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ResponseCache;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class GlobalAuthFilter implements GlobalFilter {

    AntPathMatcher matcher = new AntPathMatcher();

    @Autowired
    AuthUrlProperties urlProperties;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


//        1.获取请求路径 /order/xxx
        String path = exchange.getRequest().getURI().getPath();
        String uri = exchange.getRequest().getURI().toString();
        log.info("{} 请求开始",path);

//2.无需登录就能访问的资源：直接放行
        for (String url : urlProperties.getNoAuthUrl()) {
            boolean match = matcher.match(url, path);
            if (match){
                return chain.filter(exchange);
            }

        }

// 静态资源虽然带走了token,不用校验token,直接收
//        能走到这里，说明不是直接放行的资源
//        3.只要是/api/inner/的全部拒绝
        for (String url : urlProperties.getDenyUrl()) {
            boolean match = matcher.match(url, path);
            if (match){
//                直接响应json数据即可
                Result<String> result = Result.build("", ResultCodeEnum.PERMISSION);
                return responseResult(result,exchange);
            }
        }

//        4.需要登陆的请求：进行权限验证
        for (String url : urlProperties.getLoginAuthUrl()) {
            boolean match = matcher.match(url, path);
            if (match){
//                登陆等校验
//                3.1 获取token信息
               String tokenValue = getTokenValue(exchange);
//               3.2 校验 token
                UserInfo info =getTokenUserInfo(tokenValue);
//                3.3 判断用户信息是否正确
                if (info !=null){
//                    redis中有此用户，exchange里面的request的头会新增一个userid
                   ServerWebExchange webExchange= userIdTransport(info,exchange);
                   return chain.filter(webExchange);
                }else {
//                    redis中无此用户【假令牌，token没有，没登陆】
//                    重定向到登录页
                    return redirectToCustomPage(urlProperties.getLoginPage()+"?originUrl="+uri,exchange);
                }
            }
        }
//能走到这儿，既不是静态资源直接放行，也不是必须登录才能访问的，就一普通请求
//        普通请求只要带了token，说明可能登陆了，只要登陆了，就透传用户id
        String tokenValue = getTokenValue(exchange);
        UserInfo info = getTokenUserInfo(tokenValue);
        if (info!=null){
            exchange=userIdTransport(info,exchange);
        }else {
//            如果前端带了token，还是没用户信息代表这是假令牌
            if (!StringUtils.isEmpty(tokenValue)){
//                重新定向到登录，可以不带token，要带就得带正确
                return redirectToCustomPage(urlProperties.getLoginPage()+"?originUrl="+uri,exchange);
            }
        }


        return chain.filter(exchange);
    }

//    用户id透传
    private ServerWebExchange userIdTransport(UserInfo info, ServerWebExchange exchange) {
if (info !=null){
//    请求一旦发来，所有的请求数据都是固定的不能进行任何修改，只能读取
    ServerHttpRequest request = exchange.getRequest();

//    根据原来的请求，封装一个新请求
    ServerHttpRequest newReg = exchange.getRequest()
            .mutate()//变一个新的
            .header(SysRedisConst.USERID_HEADER,info.getId().toString())
            .build();//添加自己的头

//    放行的时候传改掉的exchange
    ServerWebExchange webExchange = exchange.mutate()
            .request(newReg)
            .response(exchange.getResponse())
            .build();
    return webExchange;

}
return exchange;
    }

    //    根据token的值去redis中查到 用户信息
    private UserInfo getTokenUserInfo(String tokenValue) {
        String json = redisTemplate.opsForValue().get(SysRedisConst.LOGIN_USER + tokenValue);
        if (!StringUtils.isEmpty(json)){
            return Jsons.toObj(json,UserInfo.class);
        }
        return null;
    }

//    从cookie或请求头中取到token对应的值

    private String getTokenValue(ServerWebExchange exchange) {
//        由于前端乱写，到处可能都有
//        1.先检查cookie中有没有这个token
        String tokenValue="";
        HttpCookie token = exchange.getRequest()
                .getCookies()
                .getFirst("token");
        if (token !=null){
            tokenValue=token.getValue();
            return tokenValue;
        }

//        2.说明cookie中没有
        tokenValue = exchange.getRequest()
                .getHeaders()
                .getFirst("token");

        return tokenValue;
    }


    //    响应一个结果
    private Mono<Void> responseResult(Result<String> result, ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        String jsonStr = Jsons.toStr(result);

        DataBuffer dataBuffer = response.bufferFactory().wrap(jsonStr.getBytes(StandardCharsets.UTF_8));

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(dataBuffer));

    }

//    重定向到指定位置
    private Mono<Void> redirectToCustomPage(String location
    ,ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();
//        1.重定向【302状态码+ 响应头中 location新位置】
        response.setStatusCode(HttpStatus.FOUND);

        response.getHeaders().add(HttpHeaders.LOCATION,location);

//        2.清楚旧的错误cookie[token] 解决无限重定向问题
        ResponseCookie tokenCookie = ResponseCookie.from("token", "777")
                .maxAge(0)
                .path("/")
                .domain(".gmall.com")
                .build();

//        3.响应结束
        return response.setComplete();
    }
}
