package com.itheima.mp.controller;


import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping
    @ApiOperation("新增用户")
    public void saveUser(@RequestBody UserFormDTO userFormDTO) {
        userService.saveUser(userFormDTO);
        log.info("新增用户成功 {}", userFormDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public void deleteUser(@ApiParam("id") @PathVariable("id") Long id) {
        userService.deleteUser(id);
        log.info("删除用户成功 id :{}", id);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询用户")
    public UserVO getUser(@ApiParam("id") @PathVariable("id") Long id) {
        return userService.queryUserAndAddressById(id);
    }

    @GetMapping
    @ApiOperation("查询用户列表")
    public List<UserVO> queryUserByIds(@ApiParam("用户id列表") @RequestParam("ids") List<Long> ids) {
        return userService.queryUserAndAddressByIds(ids);
    }

    @GetMapping("/list")
    @ApiOperation("根据复杂条件查询用户列表")
    public List<UserVO> queryUser(UserQuery userQuery) {
        return userService.queryUser(userQuery);
    }

    @PutMapping
    @ApiOperation("扣减用户余额")
    public void deductMoneyById(@ApiParam("用户id") @RequestParam("id") Long id,
                                @ApiParam("金额") @RequestParam("money") Integer money) {
        userService.deductMoneyById(id, money);
    }

    @GetMapping("/page")
    @ApiOperation("根据复杂条件分页查询用户列表")
    public PageDTO<UserVO> queryUserPage(UserQuery userQuery) {
        return userService.queryUserPage(userQuery);
    }


}
