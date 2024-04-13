package com.lxsc.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.lxsc.Code;
import com.lxsc.JsonResult;
import com.lxsc.user.model.Address;
import com.lxsc.user.model.User;
import com.lxsc.user.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;

@RestController
@CrossOrigin
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @RequestMapping("/login")
    public Object login(User user){
        //此处根据user数据在数据库中是否存在返回0或1或2
        int result=userService.login(user);
        long errorNum=0;
        String loginErrorNumStr=stringRedisTemplate.opsForValue().get("loginErrNum:"+user.getPhone());
        if(loginErrorNumStr!=null&&Long.valueOf(loginErrorNumStr)>=5){
            return new JsonResult(Code.ERROR,"超过最大次数明天再登录","");
        }
        switch(result){
            case 1:
                //用户锁定部分,相当于把键存入redis值赋1，errorNum的值也为1
                /*long errorNum=stringRedisTemplate.opsForValue().increment("loginErrNum:"+user.getPhone());
                if(errorNum==1){
                    lockUser(user.getPhone());
                }*/
                return new JsonResult(Code.ERROR,"账号错误",result);
            case 2:
                //用户锁定部分
                /*long errorNum=stringRedisTemplate.opsForValue().increment("loginErrNum:"+user.getPhone());
                if(errorNum==1){
                    lockUser(user.getPhone());
                }*/
                return new JsonResult(Code.ERROR,"密码错误",result);
        }
        //通用唯一识别码，通常用于形成主键
        String token= UUID.randomUUID().toString().replaceAll("-","");
        Duration timeout=Duration.ofSeconds(60*30);
        //将对象转变为json字符串搭配token值存入redis
        stringRedisTemplate.opsForValue().set("userToken:"+token, JSONObject.toJSONString(user));
        //将用户信息放入Map集合中
        Map resultData=new HashMap();
        resultData.put("token",token);
        resultData.put("phone",user.getPhone());
        resultData.put("nickName",user.getNickName());
        resultData.put("id",user.getId());
        //stringRedisTemplate.delete("loginErrNum:"+user.getPhone());
        return new JsonResult(Code.OK,"登陆成功",resultData);
    }

    @RequestMapping("/reg")
    public Object reg(User user){
        if(user.getPhone().equals("")){
            return new JsonResult(Code.ERROR,"手机号不能为空","");
        }
        if(user.getPassword().equals("")){
            return new JsonResult(Code.ERROR,"密码不能为空","");
        }
        int result=userService.regUser(user);
        if(result==1){
            return new JsonResult(Code.ERROR,"手机号已存在","");
        }
        //注册成功后依旧存redis，实现注册后直接登录
        String token= UUID.randomUUID().toString().replaceAll("-","");
        Duration timeout=Duration.ofSeconds(60*30);
        stringRedisTemplate.opsForValue().set("userToken:"+token, JSONObject.toJSONString(user));
        //存储所有用户信息，包括id（从数据库新返回的）
        Map resultData=new HashMap();
        resultData.put("token",token);
        resultData.put("phone",user.getPhone());
        resultData.put("nickName",user.getNickName());
        resultData.put("id",user.getId());
        return new JsonResult(Code.OK,"注册成功",resultData);
    }

    @GetMapping("/getUserId")
    public Object getUserId(String token){
        String userJson=stringRedisTemplate.opsForValue().get("userToken:"+token);
        if(userJson==null){
            return new JsonResult<Long>(Code.ERROR,null);
        }
        /*Duration timeout=Duration.ofSeconds(60*30);
        stringRedisTemplate.expire("userToken"+token,timeout);*/
        Long userId= JSONObject.parseObject(userJson).getBigInteger("id").longValue();
        return new JsonResult(Code.OK,userId);
    }

    @GetMapping("/confirmUserAddress")
    public Object confirmUserAddress(String token){
        String userJson=checkUser(token);
        if(userJson==null){
            return new JsonResult<Long>(Code.ERROR,null);
        }
        Long userId= JSONObject.parseObject(userJson).getBigInteger("id").longValue();
        List<Address> list=userService.getUserAddressByUserId(userId);
        return new JsonResult(Code.OK,list);
    }


    //输错一次上锁，即为redis中错误信标设置过期时间
    private void lockUser(String phone){
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        Duration timeout=Duration.ofSeconds(calendar.getTimeInMillis()/1000-System.currentTimeMillis()/1000);
        stringRedisTemplate.expire("loginErrNum:"+phone,timeout);
    }

    private String checkUser(String token){
        String userJson=stringRedisTemplate.opsForValue().get("userToken:"+token);
        if(userJson==null){
            return null;
        }
        /*Duration timeout=Duration.ofSeconds(60*30);
        stringRedisTemplate.expire("userToken"+token,timeout);*/
        return userJson;
    }


}
