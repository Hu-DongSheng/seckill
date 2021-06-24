package com.org.service.impl;

import com.org.bean.Seckill;
import com.org.dto.Exposer;
import com.org.dto.SeckillExecution;
import com.org.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-core.xml"})
public class SeckillServiceImplTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() {
        Seckill seckill = seckillService.getById(1001);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() {
        Exposer exposer = seckillService.exportSeckillUrl(1004);
        logger.info("{}",exposer);
    }

    @Test
    public void executeSeckill() {
        SeckillExecution seckillExecution = seckillService.ExecuteSeckill(1004, 13511113333L,
                "beabee1e3e1f6c9be4f2de808674e3b3");
        logger.info("{}",seckillExecution);
    }

    @Test
    public void executeSeckillProcedure() {
        long seckillId = 1005;
        long phone = 11122233313L;

        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.ExecuteSeckillProcedure(seckillId, phone, md5);
            System.out.println(execution.getStateInfo());

        }
    }
}