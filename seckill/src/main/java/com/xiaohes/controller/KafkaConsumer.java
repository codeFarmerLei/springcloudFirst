package com.xiaohes.controller;

import com.xiaohes.common.bean.Result;
import com.xiaohes.service.ISeckillService;
import com.xiaohes.webSocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author by lei
 * @date 2019-1-18 11:51
 */
@Component
public class KafkaConsumer {

    private final static Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    @Autowired
    ISeckillService seckillService;

    /**
     * 监听seckill主题,有消息就读取
     * @param message
     */
    @KafkaListener(topics = "seckill")
    public void receiveMessage(String message){
        //收到通道的消息之后执行秒杀操作
        String[] array = message.split(";");

        //可以注释掉上面的使用这个测试
        Result result = seckillService.startSeckilRedisLock(Long.parseLong(array[0]), Long.parseLong(array[1]));
        String resultStr = null;
        if(result.equals(Result.success())){
            resultStr = "秒杀成功";
        }else{
            resultStr = "秒杀失败";
        }
        WebSocketServer.sendInfo(resultStr,array[1]);//推送给前台
        log.info("====================={}{}=====================",message,resultStr);
    }
}
