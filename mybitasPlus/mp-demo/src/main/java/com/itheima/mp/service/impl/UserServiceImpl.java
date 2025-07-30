package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


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
    @Transactional
    public void deductMoneyById(Long id, Integer money) {
        User user = getById(id);
        if (user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户不存在或已禁用");
        }
        if (user.getBalance() < money) {
            throw new RuntimeException("余额不足");
        }

//        user.setBalance(user.getBalance() - money);
//        updateById(user);
//        baseMapper.deductMoneyById(id, money);
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, UserStatus.FROZEN)
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance()) // 乐观锁
                .update();


    }

    @Override
    public List<UserVO> queryUser(UserQuery userQuery) {
        List<User> users = lambdaQuery()
                .like(userQuery.getName() != null, User::getUsername, userQuery.getName())
                .eq(userQuery.getStatus() != null, User::getStatus, userQuery.getStatus())
                .ge(userQuery.getMinBalance() != null, User::getBalance, userQuery.getMinBalance())
                .le(userQuery.getMaxBalance() != null, User::getBalance, userQuery.getMaxBalance())
                .list();
        return BeanUtil.copyToList(users, UserVO.class);
    }

    @Override
    public UserVO queryUserAndAddressById(Long id) {
        User user = getById(id);
        if (user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户不存在或已禁用");
        }
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id).list();

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if (CollUtil.isNotEmpty(addresses)) {
            List<AddressVO> addressesVO = BeanUtil.copyToList(addresses, AddressVO.class);
            userVO.setAddresses(addressesVO);
        }

        return userVO;
    }

    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        List<User> userList = listByIds(ids);
        if (CollUtil.isEmpty(userList)) {
            return Collections.emptyList();
        }

        List<Long> userIds = userList.stream().map(User::getId).collect(Collectors.toList());
        List<Address> addresses = Db.lambdaQuery(Address.class).in(Address::getUserId, userIds).list();
        List<AddressVO> addressVOS = BeanUtil.copyToList(addresses, AddressVO.class);
        Map<Long, List<AddressVO>> userAddress = new HashMap<>(0);
        if (CollUtil.isNotEmpty(addressVOS)) {
            userAddress = addressVOS.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        List<UserVO> userVOS = new ArrayList<>(userList.size());
        for (User user : userList) {
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            userVO.setAddresses(userAddress.get(user.getId()));
            userVOS.add(userVO);
        }

        return userVOS;
    }

    @Override
    public PageDTO<UserVO> queryUserPage(UserQuery userQuery) {

//        Page<User> page = Page.of(userQuery.getPageNo(), userQuery.getPageSize());
//
//        if (StrUtil.isNotBlank(userQuery.getSortBy())) {
//            page.addOrder(new OrderItem(userQuery.getSortBy(), userQuery.getIsAsc()));
//        }else {
//            page.addOrder(new OrderItem("update_time", false));
//        }

        Page<User> page = userQuery.toMpPageDefaultSortByUpdateTimeDesc();

        Page<User> userPage = lambdaQuery()
                .like(userQuery.getName() != null, User::getUsername, userQuery.getName())
                .eq(userQuery.getStatus() != null, User::getStatus, userQuery.getStatus())
                //                .ge(userQuery.getMinBalance() != null, User::getBalance, userQuery.getMinBalance())
                //                .le(userQuery.getMaxBalance() != null, User::getBalance, userQuery.getMaxBalance())
                .page(page);

//        PageDTO<UserVO> results = new PageDTO<>();
//        List<User> records = userPage.getRecords();
//        results.setTotal(page.getTotal());
//        results.setPages(page.getPages());
//        if (CollUtil.isEmpty(records)) {
//            return PageDTO.empty(userPage);
//        }
//
//        List<UserVO> userVOS = BeanUtil.copyToList(records, UserVO.class);
//        results.setList(userVOS);
//
//        return results;

        return PageDTO.of(userPage, user -> BeanUtil.copyProperties(user, UserVO.class));
    }
}
