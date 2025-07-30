package com.itheima.mp.service;

import com.itheima.mp.domain.po.Address;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IAddressServiceTest {

    @Resource
    private IAddressService addressService;

    @Test
    void testDelete() {
        addressService.removeById(59L);

        Address address = addressService.getById(59L);
        System.out.println(address);

    }

}