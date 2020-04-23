package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式
 * */
public class Producer04_topics {

    //队列
    private static final String QUEUE_INFORM_EMAIL = "quee_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";
    //下面#是通配符
    private static final String ROUTINGKEY_EMAIL = "inform.#.email.#";
    private static final String ROUTINGKEY_SMS= "inform.#.sms.#";

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
            //(1)声明两个队列
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
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
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);

            //（3）进行交换机和队列绑定
            //参数：String queue,String exchange,String routingKey
            /*
             *参数明细：
             * 1.queue 队列名称
             * 2.exchange交换机名称
             * 3.路由key，根据key之将消息转发到指定的队列中，在发布订阅模式中协调为空串，不需要
             * 也就是发送消息的时候，如果检测到符合绑定时候的规则，则入对应绑定
             */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_TOPICS_INFORM,ROUTINGKEY_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_TOPICS_INFORM,ROUTINGKEY_SMS);

            //3。发送消息
            //参数：String exchange,String routingKey,BasicProperites,byte[] body
            /**参数明细：
             * exchange:交换机，如果不指定将使用mq的默认交换机(设置为“”)
             * routingKey：路由key,交换机根据路由key来将消息转发到指定的队列,如果使用默认交换机，routingKey设置为队列名称
             * props：消息的属性
             * body：消息内容
             * */
            for(int i=0;i<5;i++){
                //消息内容
                String message = "send email inform message to user";
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.email",null,message.getBytes());
                System.out.println("send to mq"+message);
            }

            for(int i=0;i<5;i++){
                //消息内容
                String message = "send sms inform message to user";
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.sms",null,message.getBytes());
                System.out.println("send to mq"+message);
            }

            for(int i=0;i<5;i++){
                //消息内容
                String message = "send email,sms inform message to user";
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.sms.email",null,message.getBytes());
                System.out.println("send to mq"+message);
            }

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
