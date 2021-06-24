package com.org.dao;

import com.org.bean.SuccessKilled;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入影响行数
     */
    int insertSuccessKilled(@Param("seckillId")long seckillId, @Param("userPhone")long userPhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId, @Param("userphone")long userphone);

    void killByProcedure(Map<String,Object> map);
}
