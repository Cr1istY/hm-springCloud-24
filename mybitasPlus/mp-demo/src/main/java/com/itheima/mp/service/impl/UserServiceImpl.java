package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public void saveUser(UserFormDTO userFormDTO) {
        save(BeanUtil.copyProperties(userFormDTO, User.class));
    }

    @Override
    public void deleteUser(Long id) {
        removeById(id);
    }

    @Override
    public UserVO getUser(Long id) {
        User user = getById(id);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @Override
    public List<UserVO> queryUserByIds(List<Long> ids) {
        List<User> userList = listByIds(ids);
        return BeanUtil.copyToList(userList, UserVO.class);
    }

    @Override
    public void deductMoneyById(Long id, Integer money) {
        User user = getById(id);
        if (user == null || user.getStatus() == 2) {
            throw new RuntimeException("用户不存在或已禁用");
        }
        if (user.getBalance() < money) {
            throw new RuntimeException("余额不足");
        }

//        user.setBalance(user.getBalance() - money);
//        updateById(user);
        baseMapper.deductMoneyById(id, money);
    }
}
