package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;

import java.util.List;

public interface IUserService extends IService<User> {
    void saveUser(UserFormDTO userFormDTO);

    void deleteUser(Long id);

    UserVO getUser(Long id);

    List<UserVO> queryUserByIds(List<Long> ids);

    void deductMoneyById(Long id, Integer money);

    List<UserVO> queryUser(UserQuery userQuery);

    UserVO queryUserAndAddressById(Long id);

    List<UserVO> queryUserAndAddressByIds(List<Long> ids);

    PageDTO<UserVO> queryUserPage(UserQuery userQuery);
}
