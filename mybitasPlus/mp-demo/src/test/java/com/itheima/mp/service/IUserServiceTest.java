package com.itheima.mp.service;

import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.time.LocalDateTime;


@SpringBootTest
class IUserServiceTest {

    @Resource
    private IUserService userService;

    @Test
    void testSave() {
        User user = new User();
        user.setUsername("张三");
        user.setPassword("123456");
        user.setPhone("12345678901");
        user.setInfo("{\"age\": 20, \"intro\": \"语文老师\", \"gender\": \"male\"}");
        user.setBalance(100);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        boolean result = userService.save(user);
        System.out.println("保存成功：" + result);
        System.out.println();
    }

}