package com.bxy.rabbitmq;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitMQ
 * 入门程序
 */
public class Producer01 {
    //队列名称
    private static final String QUEUE_INFO_EMAIL = "QUEUE_INFO_EMAIL";
    private static final String QUEUE_INFO_SMS = "QUEUE_INFO_SMS";
    private static final String EXCHANGE_FANOUT_INFORM = "EXCHANGE_FANOUT_INFORM";

    public static void main(String[] args) {
        //生产者需要和MQ建立连接
        //通过连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setPort(5672);
        connectionFactory.setHost("106.14.222.88");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        //设置虚拟机 为了设置一个mq服务可以设置多个虚拟机 每个虚拟机就相当于一个独立的mq（队列）
        connectionFactory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;
        try {
            //建立新链接
            connection = connectionFactory.newConnection();
            //建立通道  所有通信都在这个channel中
            channel = connection.createChannel();
            //声明队列
            //参数：String var1, boolean var2, boolean var3, boolean var4, Map<String, Object> var5)
            //   1.队列名称     2.是否持久化，如果持久化,mq重启后还在
            //   3.是否独占连接,如果true连接关闭后自动删除，   4.队列不再使用时是否自动删除，用于临时队列创建
            //   5.队列的扩展参数  比如：设置存活时间
            channel.queueDeclare(QUEUE_INFO_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFO_SMS, true, false, false, null);
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
            /**
             * 将交换器与队列通过路由键绑定
             */
            //String var1, String var2, String var3
            //队列名 交换机名 路由key 在发布订阅模式中为空
            channel.queueBind(QUEUE_INFO_EMAIL,EXCHANGE_FANOUT_INFORM,"");
            channel.queueBind(QUEUE_INFO_SMS,EXCHANGE_FANOUT_INFORM,"");

            //发送消息需要制定交换机
            //参数：(String var1, String var2, BasicProperties var3, byte[] var4)
            //   1.指定交换机 不指定用默认     2.路由key 如果使用默认交换机 设置成队列名称即刻
            //   3. 消息属性     4.消息内容
            String message = "我想死";
            channel.basicPublish(EXCHANGE_FANOUT_INFORM, QUEUE_INFO_EMAIL, null, message.getBytes());
            System.out.println("send mqqqqqq" + message);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
