package com.xinnn.reggie.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.xinnn.reggie.config.StpUserUtil;
import com.xinnn.reggie.pojo.User;
import com.xinnn.reggie.service.UserService;
import com.xinnn.reggie.utils.AliyunEmailCode;
import com.xinnn.reggie.utils.RedisUtil;
import com.xinnn.reggie.utils.Result;
import com.xinnn.reggie.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 阿里云发送邮箱验证码
     * @param map 获取前端传过来的email地址
     * @return 返回验证码发送状态
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody Map<String, Object> map){
        String email = (String) map.get("email");
        String code = StringUtil.makeCode();
        if (email != null){
//            try{
//                //阿里云发送邮箱验证码
//                AliyunEmailCode.main(email, code);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            log.info(code);
            /*
            将验证码保存到session
            session.setAttribute(email, code);
             */
            //将验证码保存到redis 设置过期时间5分钟
            String key = RedisUtil.REGGIE_KEY + email;
            redisUtil.set(key, code, 5);
            return Result.success("发送验证码成功！");
        }
        return Result.error("发送验证码失败！");
    }

    /**
     * 移动端用户登陆方法
     * @param map 获取用户的email地址和验证码
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, Object> map){
        String email = (String) map.get("phone");
        String userCode = (String) map.get("code");
        //从session中获取验证码
        //String code =(String) session.getAttribute(email);
        String code = String.valueOf(redisUtil.get(RedisUtil.REGGIE_KEY + email));
        if (code == null){
            return Result.error("你还没有获取验证码");
        }
        if (!code.equals(userCode)){
            return Result.error("验证码错误");
        }
        User user = userService.getUserByPhone(email);
        if (user == null){
            //如果数据库中没有此用户 那么就将该用户添加到数据库
            user = new User();
            user.setPhone(email);
            user.setStatus(1);
            userService.save(user);
        }
        //从session中删除验证码
        //session.removeAttribute(email);
        //从redis中删除验证码
        redisUtil.remove(RedisUtil.REGGIE_KEY + email);
        StpUserUtil.login(user.getId());
        StpUserUtil.getSession().set("userId", user.getId());
        return Result.success(user);
    }

    /**
     * 用户退出登陆
     * @return
     */
    @PostMapping("/loginout")
    public Result<String> userLoginout(){
        Long id = StpUserUtil.getSession().getLong("userId");
        StpUserUtil.logout(id);
        StpUserUtil.getSession().delete("userId");
        return Result.success("退出登陆成功");
    }
}
