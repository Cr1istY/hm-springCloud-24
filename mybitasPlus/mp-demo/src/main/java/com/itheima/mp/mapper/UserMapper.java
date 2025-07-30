package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


public interface UserMapper extends BaseMapper<User> {
    @Update("update user set balance = balance - #{money} where id = #{id}")
    void deductMoneyById(@Param("id") Long id,@Param("money") Integer money);
}
