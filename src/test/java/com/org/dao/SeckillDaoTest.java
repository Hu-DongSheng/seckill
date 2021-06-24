package com.org.dao;

import com.org.bean.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-core.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill.toString());

    }

    @Test
    public void reduceNumber() {
        Date killTime = new Date();
        int update = seckillDao.reduceNumber(1000L,killTime);
        System.out.println("update="+ update);
    }

    @Test
    public void queryAll() {
        List<Seckill> list = seckillDao.queryAll(0,100);
        for(Seckill seckill : list){
            System.out.println(seckill);
        }
    }
}