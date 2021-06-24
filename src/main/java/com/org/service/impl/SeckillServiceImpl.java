package com.org.service.impl;

import com.org.bean.Seckill;
import com.org.bean.SuccessKilled;
import com.org.dao.SeckillDao;
import com.org.dao.SuccessKilledDao;
import com.org.dao.cache.RedisDao;
import com.org.dto.Exposer;
import com.org.dto.SeckillExecution;
import com.org.enums.SeckillStateEnum;
import com.org.exception.RepeatKillException;
import com.org.exception.SeckillCloseException;
import com.org.exception.SeckillException;
import com.org.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillDao seckillDao;

    @Resource
    private SuccessKilledDao successKilledDao;

    @Resource
    private RedisDao redisDao;

    private final String slat = "*()#^HRIOU0-fIotpw0-ri0-sikdo";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,Integer.MAX_VALUE);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化,超时的基础上维护一致性
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            seckill = seckillDao.queryById(seckillId);
            if(seckill == null){
                return new Exposer(false, seckillId);
            }else{
                redisDao.putSeckill(seckill);
            }
        }
        long nowTime = new Date().getTime();
        long startTime = seckill.getStartTime().getTime();
        long endTime = seckill.getEndTime().getTime();
        if(nowTime < startTime || nowTime > endTime){
            return new Exposer(false,seckillId,nowTime,startTime,endTime);
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }


    @Override
    @Transactional
    public SeckillExecution ExecuteSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date now = new Date();
        try {
            if (successKilledDao.insertSuccessKilled(seckillId, userPhone) <= 0) {
                throw new SeckillCloseException("seckill is close");
            } else {
                if (seckillDao.reduceNumber(seckillId, now) <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        }catch (RepeatKillException e1){
            throw e1;
        }catch (SeckillCloseException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error : " + e.getMessage() );
        }
    }

    @Override
    public SeckillExecution ExecuteSeckillProcedure(long seckillId, long userPhone, String md5) {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date now = new Date();
        Map<String,Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime",now);
        map.put("result",null);
        try{
            successKilledDao.killByProcedure(map);
            //获取result
            int result = MapUtils.getInteger(map,"result",-2);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
            } else {
                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }
}
