package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //因为有前面RefreshToken拦截器的存在，只需要根据ThreadLocalhost中是否有这个用户来进行拦截
        if(UserHolder.getUser() == null){
            //没有，则拦截
            response.setStatus(401);
            return false;
        }//有用户，放行
        return true;
    }
}
