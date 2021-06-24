package com.org.controller;

import com.org.bean.Seckill;
import com.org.dto.Exposer;
import com.org.dto.SeckillExecution;
import com.org.dto.SeckillResult;
import com.org.enums.SeckillStateEnum;
import com.org.exception.RepeatKillException;
import com.org.exception.SeckillCloseException;
import com.org.exception.SeckillException;
import com.org.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    /**
     * 所有商品列表页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list );
        return "list";
    }

    /**
     * 单个商品详情页
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId")Long seckilId, Model model){
        if(seckilId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckilId);
        if(seckill == null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 秒杀地址
     */
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execution(@PathVariable("seckillId") Long seckillId,
                                                     @CookieValue(value = "killPhone",required = false) Long userPhone,
                                                     @PathVariable("md5") String md5){
        System.out.println(userPhone);
        if(userPhone == null){
            return new SeckillResult<>(false,"未注册");
        }
        SeckillExecution execution;
        try {
            execution = seckillService.ExecuteSeckillProcedure(seckillId,userPhone,md5);
            return new SeckillResult<>(true ,execution);
        }catch (RepeatKillException e){
            execution = new SeckillExecution(seckillId,SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<>(true, execution);
        }catch (SeckillCloseException e) {
            execution = new SeckillExecution(seckillId,SeckillStateEnum.END);
            return new SeckillResult<>(true,execution);
        }
        catch (SeckillException e) {
            execution = new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<>(true,execution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        return new SeckillResult<>(true, new Date().getTime());
    }
}
