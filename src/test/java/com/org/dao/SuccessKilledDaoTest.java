package com.org.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-core.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        System.out.println(successKilledDao.insertSuccessKilled(1001L,13500001111L));
    }

    @Test
    public void queryByIdWithSeckill() {
        System.out.println(successKilledDao.queryByIdWithSeckill(1000,13500001111L).toString());
    }
}