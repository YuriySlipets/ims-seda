package ua.kpi.receiver;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import ua.kpi.RoutingInfo;

@SpringBootApplication
public class RabbitmqReceiverRunner implements CommandLineRunner {

    @Autowired
    AnnotationConfigApplicationContext context;

    @Autowired
    Receiver receiver;

    @Bean
    public RoutingInfo routingInfo(@Value("${messageType}") final String messageType){
        return RoutingInfo.valueOf(messageType);
    }

    @Bean
    public Receiver receiver(RoutingInfo routingInfo) {
        return new Receiver(routingInfo);
    }

    @Bean
    public Exchange exchange(RoutingInfo routingInfo) {
        return new DirectExchange(routingInfo.getExchange());
    }

    @Bean
    public Queue queue(RoutingInfo routingInfo) {
        return new Queue(routingInfo.getDestination(), false);
    }

    @Bean
    public Binding binding(RoutingInfo routingInfo) {
        return new Binding(
                routingInfo.getDestination(),
                Binding.DestinationType.QUEUE,
                routingInfo.getExchange(),
                routingInfo.getKey(),
                null
        );
    }

    @Bean
    public MessageListenerContainer containerForMessageType1(ConnectionFactory connectionFactory, Receiver receiver, RoutingInfo routingInfo) {
        final MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        container.setQueueNames(routingInfo.getDestination());
        container.setMessageListener(listenerAdapter);

        return container;
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqReceiverRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            receiver.sendShutdownSignal();
        }));
        receiver.run();
        context.close();
    }
}
