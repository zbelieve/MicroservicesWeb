package com.xuecheng.test.rabbitmq;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq入门程序,生产者
 * Work queues工作模式
 * */
public class Producer01 {
    //队列
    private static final String QUEUE = "helloworld";

    public static void main(String[] args) throws IOException, TimeoutException {
        //通过连接工厂创建新的连接和mq进行连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);//记住这个端口和web端管理页面那个端口不一样
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机，一个人mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");
        //建立新连接
        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            //1。创建会话通道，生产者和mq服务所有通信都在channel通道中完成
            channel = connection.createChannel();

            //2。声明队列，如果队列在mq中没有则要创建
            //参数：String queue,boolean durable,boolean exclusive,boolean autoDelete,Map<String Object>arguments
            /**参数细节
             * 1。queue队列名称
             * 2。durable是否持久化，如果持久化,mq重启后队列还在
             * 3。exclusive是否独占连接，队列只允许在该连接中访问，如果connection连接关闭队列则自动删除，如果将此参数设置未true可用于临时队列的创建
             * 4。autoDelete自动删除，队列不再使用时是否自动删除此队列，如果将此参数和exclusive参数设置为true就可以实现临时队列（队列不用了就可以自动删除）
             * 5。arguments参数，可以设置一个队列的扩展参数，比如：可设置存活时间
             *
             */
            channel.queueDeclare(QUEUE,true,false,false,null);

            //3。发送消息
            //参数：String exchange,String routingKey,BasicProperites,byte[] body
            /**参数明细：
             * exchange:交换机，如果不指定将使用mq的默认交换机(设置为“”)
             * routingKey：路由key,交换机根据路由key来将消息转发到指定的队列,如果使用默认交换机，routingKey设置为队列名称
             * props：消息的属性
             * body：消息内容
            * */
            //消息内容
            String message = "hello world 黑马程序员";
            channel.basicPublish("",QUEUE,null,message.getBytes());
            System.out.println("send to mq"+message);
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            if(channel != null)
            {
                channel.close();
            }
            if(connection != null)
            {
                connection.close();
            }
        }


    }
}
