package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testQueryMapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id", "username", "phone", "info", "balance", "create_time", "update_time")
                .like("username", "o")
                .ge("balance", 1000);
        List<User> userList = userMapper.selectList(wrapper);
        userList.forEach(System.out::println);
    }

    @Test
    void testLambdaQueryMapper() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getBalance, User::getUsername)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);

        List<User> userList = userMapper.selectList(wrapper);
        userList.forEach( user -> {
            System.out.println("Username:" + user.getUsername() + "\tBalance:" + user.getBalance());
        });

    }


    @Test
    void testInsert() {
        User user = new User();
//        user.setId(5L);
        user.setUsername("Crist");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
//        user.setInfo("{\"age\": 20, \"intro\": \"语文老师\", \"gender\": \"male\"}");
        user.setInfo(UserInfo.of(20, "语文老师", "male"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(5L);
    }
}