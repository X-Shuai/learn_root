package com.xs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2021-03-24 23:41
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMqTest {

    @Resource
    RabbitTemplate rabbitTemplate;

    @Test
    public void testSendByTopics(){
        for (int i=0;i<5;i++){
            String message = "sms email inform to user"+i;
            rabbitTemplate.convertAndSend("inform.sms.email",message);
            System.out.println("Send Message is:'" + message + "'");
        }
    }


}
