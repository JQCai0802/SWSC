package com.lxsc.orders.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    @Bean
    public FanoutExchange orderPay(){
        return new FanoutExchange("orderPayExchange");
    }
    @Bean
    public Queue pointQueue(){
        return new Queue("pointQueue");
    }
    @Bean
    public Queue wuLiu(){
        return new Queue("wuLiuQueue");
    }
    @Bean
    public Binding pointBinding(){
        return new Binding("pointQueue",Binding.DestinationType.QUEUE,"orderPayExchange","",null);
    }
    @Bean
    public Binding wuLiuBinding(){
        return new Binding("wuLiuQueue",Binding.DestinationType.QUEUE,"orderPayExchange","",null);
    }
    //A
    @Bean
    public Queue orderQueue(){
        Map<String, Object> arguments=new HashMap<String, Object>();
        arguments.put("x-dead-letter-exchange","orderDeadLetterExchange");
        arguments.put("x-dead-letter-routing-key","orderDeadLetterKey");
        return new Queue("orderQueue",true,false,false,arguments);
    }
    @Bean
    public FanoutExchange orderExchange(){
        return new FanoutExchange("orderExchange");
    }
    @Bean
    public Binding orderBinding(){
        return new Binding("orderQueue",Binding.DestinationType.QUEUE,"orderExchange","",null);
    }
    //B
    @Bean
    public Queue orderDeadLetterQueue(){
        return new Queue("orderDeadLetterQueue");
    }
    @Bean
    public DirectExchange orderDeadLetterExchange(){
        return new DirectExchange("orderDeadLetterExchange");
    }
    @Bean
    public Binding orderDeadLetterBinding(){
        return new Binding("orderDeadLetterQueue", Binding.DestinationType.QUEUE,"orderDeadLetterExchange","orderDeadLetterKey",null);
    }












}
