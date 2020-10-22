package com.bxy.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class Consumer_Email {
    //队列名称
    private static final String QUEUE_INFO_EMAIL = "helloWorld";
    private static final String EXCHANGE_FANOUT_INFORM = "helloWorld";
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
        try {
            //建立新链接
            connection = connectionFactory.newConnection();
            //建立通道  所有通信都在这个channel中
            Channel channel = connection.createChannel();
            channel.queueBind(EXCHANGE_FANOUT_INFORM,QUEUE_INFO_EMAIL,"");
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM,BuiltinExchangeType.FANOUT);
            //声明队列
            //参数：String var1, boolean var2, boolean var3, boolean var4, Map<String, Object> var5)
            //   1.队列名称     2.是否持久化，如果持久化,mq重启后还在
            //   3.是否独占连接,如果true连接关闭后自动删除，   4.队列不再使用时是否自动删除，用于临时队列创建
            //   5.队列的扩展参数  比如：设置存活时间
            channel.queueDeclare(QUEUE_INFO_EMAIL, true, false, false, null);
            /*
            监听队列
            参数：String dui, boolean var2, Consumer callback, boolean var4, Map<String, Object> var5)
               1.队列名称     2.自动回复   3.消费方法
            实现消费方法
            */
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
                //String consumerTag, Envelope envelope, BasicProperties properties, byte[] body
                void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
                    String exchange = envelope.getExchange();
                    //消息的id 消息标识 可以确认消息已接受
                    long deliveryTag=envelope.getDeliveryTag();
                    String str=new String(body, "utf-8");
                    System.out.println("消息为："+str);
                }
            };

            channel.basicConsume(QUEUE_INFO_EMAIL, true,defaultConsumer );
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            /*try {
               // Objects.requireNonNull(connection).close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }
}
