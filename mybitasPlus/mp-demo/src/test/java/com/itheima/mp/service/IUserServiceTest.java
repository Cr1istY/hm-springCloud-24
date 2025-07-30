package com.itheima.mp.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.List;


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
//        user.setInfo("{\"age\": 20, \"intro\": \"语文老师\", \"gender\": \"male\"}");
        user.setInfo(UserInfo.of(20, "语文老师", "male"));
        user.setBalance(100);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        boolean result = userService.save(user);
        System.out.println("保存成功：" + result);
        System.out.println();
    }


    @Test
    void testPage() {
        Page<User> page = Page.of(1, 5);
        page.addOrder(new OrderItem("balance", false));
        page.addOrder(new OrderItem("id", true));
        Page<User> userPage = userService.page(page);
        long total = userPage.getTotal();
        System.out.println("总记录数：" + total);
        List<User> records = userPage.getRecords();
        records.forEach(System.out::println);

    }

}