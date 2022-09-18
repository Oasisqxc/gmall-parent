package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.user.LoginSuccessVo;
import com.atguigu.gmall.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserInfoService userInfoService;

//    用户登录
    @PostMapping("/passport/login")
    public Result login(@RequestBody UserInfo info
                        ){

        LoginSuccessVo vo =  userInfoService.login(info);;
        if (vo != null){
            return Result.ok(vo);

        }
        return Result.build("", ResultCodeEnum.LOGIN_ERROR);
    }

//    用户退出
    @GetMapping("/passport/logout")
    public Result logout(@RequestHeader("token") String token){

        userInfoService.logout(token);
        return Result.ok();
    }


}
