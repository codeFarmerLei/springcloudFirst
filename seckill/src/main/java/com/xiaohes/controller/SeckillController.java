package com.xiaohes.controller;

import com.github.pagehelper.PageInfo;
import com.xiaohes.common.bean.Result;
import com.xiaohes.common.utils.RedisUtil;
import com.xiaohes.service.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author by lei
 * @date 2019-1-18 9:18
 */

@RestController
public class SeckillController {

    private final static Logger log = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    ISeckillService seckillService;

    @PostMapping("/list")
    public Map<String,Object> list() {
        PageInfo<Map<String,Object>> list = seckillService.getSeckillList();

        return Result.success(list.getList());
    }

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @RequestMapping("/startSeckill")
    public Result startSeckill(String id) {
        final String sid = id;

        Runnable task = new Runnable() {
            @Override
            public void run() {

                Random rand = new Random();
                String uid = System.currentTimeMillis()+""+ rand.nextInt(10000);

                kafkaTemplate.send("seckill",sid+";"+ uid);
            }
        };
        executor.execute(task);
        return Result.success();
    }





    private static int corePoolSize = Runtime.getRuntime().availableProcessors();

    /**
     * corePoolSize：假设系统要求每秒需要处理500~1000个任务，每个任务花费0.1秒，线程数量则设置为50~100
     * workQueue：50个线程每个执行0.1秒，响应时间为1秒，则设置为(50/0.1)*1=500
     *   意思是队列里的线程可以等待2s，超过了的需要新开线程来执行
     *   如果设置为Integer.MAX_VALUE，将会导致线程数量永远为corePoolSize，再也不会增加，当任务数量陡增时，任务响应时间也将随之陡增。
     * maxPoolSize： (max(tasks)- workQueue)/(1/taskcost)=(1000-500)/(1/0.1)=50
     */
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(200));

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分布式实现
     * @param id
     * @return
     */
    @PostMapping("/startKafkaQueue")
    public Result startKafkaQueue(String id){
        final String killId =  id;
        for(int i=0;i<1000;i++){
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    if(redisUtil.get(killId)==null){
                        //思考如何返回给用户信息ws
                        kafkaTemplate.send("seckill",killId+";"+userId);
                    }else{
                        //秒杀结束
                    }
                    log.info("=========================秒杀开始================="+killId+";"+userId);

                }
            };
            executor.execute(task);
        }
        return Result.success();
    }
}
