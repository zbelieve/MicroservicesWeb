package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer02_subscribe_sms {
    //队列
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";
    public static void main(String[] args) throws IOException, TimeoutException {
        //通过连接工厂创建新的连接和mq进行连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);//记住这个端口和web端管理页面那个端口不一样
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机，一个人mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

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
        //(1)声明两个队列
        channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
        //（2）声明一个交换机
        //参数：String exchange,String type
        /*
         * 参数明细：
         * 1、交换机的名称
         * 2、交换机的类型
         * fanout：对应的rabbitmq的工作模式是public/subscribe发布订阅
         * direct:对应Routing工作模式
         * topic:通配符工作模式
         * headers:对应headers工作模式
         * */
        channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);

        //实现消费方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            //当接收到消息后此方法被调用
            /**
             * * 消费者接收消息调用此方法
             * * @param consumerTag 消费者的标签，在监听队列channel.basicConsume()去指定，可以不设置
             * * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志
             * (收到消息失败后是否需要重新发送)
             * * @param properties
             * * @param body
             * * @throws IOException
             * */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //交换机
                String exchange = envelope.getExchange();
                //消息id，mq在channel中用来表示消息的id，可用于确认消息已接受
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
                String message = new String(body,"utf-8");
                System.out.println("receive"+message);

            }
        };

        //监听队列
        //参数：监听队列String queue, boolean autoAck,Consumer callback

        /**
         *queue 队列名称
         *autoAck 是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动回复
         *callback 消费消息的方法，消费者接收到消息后调用此方法
         */
        channel.basicConsume(QUEUE_INFORM_SMS,true,defaultConsumer);

    }
}
