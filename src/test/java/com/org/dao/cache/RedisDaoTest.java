package com.org.dao.cache;

import com.org.bean.Seckill;
import com.org.dao.SeckillDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-core.xml"})
public class RedisDaoTest {
    private long id = 1001;

    @Resource
    private RedisDao redisDao;

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() {
        Seckill seckill = redisDao.getSeckill(id);
        if(seckill == null){
            seckill = seckillDao.queryById(id);
            if(seckill != null){
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill = redisDao.getSeckill(id);
                System.out.println(seckill);
            }
        }
    }
}