package com.org.service;

import com.org.bean.Seckill;
import com.org.dto.Exposer;
import com.org.dto.SeckillExecution;
import com.org.exception.RepeatKillException;
import com.org.exception.SeckillCloseException;
import com.org.exception.SeckillException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时，输出秒杀接口的地址，否则输出系统时间和秒杀开始时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution ExecuteSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, RepeatKillException, SeckillCloseException;

    //存储过程优化
    SeckillExecution ExecuteSeckillProcedure(long seckillId, long userPhone, String md5);

}
