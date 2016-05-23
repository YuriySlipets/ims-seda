package ua.kpi;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.ArrayList;
import java.util.Properties;

@SpringBootApplication
public class SedaControllerRunner implements CommandLineRunner {

    @Autowired
    private AbstractApplicationContext context;

    @Autowired
    private RabbitAdmin admin;

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    public static void main(String[] args) {
        SpringApplication.run(SedaControllerRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        final ArrayList<ReceiverRunnable> receiverRunnables = initReceiverRunnables();

//        final ExecutorService executorService = Executors.newFixedThreadPool(receiverRunnables.size());
//        receiverRunnables.forEach(executorService::execute);

        for (RoutingInfo routingInfo : RoutingInfo.values()) {
            final int queueMessageCountString = getMessageCount(routingInfo.getDestination());
            System.out.format("%nQueue '%s' message count = %d%n%n", routingInfo.getDestination(), queueMessageCountString);
        }

        context.close();
    }

    public int getMessageCount(String queueName) {
        final Properties queueProperties = admin.getQueueProperties(queueName);
        final String queueMessageCountString = queueProperties.get("QUEUE_MESSAGE_COUNT").toString();

        return Integer.parseInt(queueMessageCountString);
    }

    public ArrayList<ReceiverRunnable> initReceiverRunnables() {
        final ArrayList<ReceiverRunnable> receiverRunnables = new ArrayList<>();

        receiverRunnables.add(new ReceiverRunnable(RoutingInfo.TYPE_1));
        receiverRunnables.add(new ReceiverRunnable(RoutingInfo.TYPE_2));
        receiverRunnables.add(new ReceiverRunnable(RoutingInfo.TYPE_3));
        receiverRunnables.add(new ReceiverRunnable(RoutingInfo.TYPE_3));
        receiverRunnables.add(new ReceiverRunnable(RoutingInfo.TYPE_3));
        return receiverRunnables;
    }
}
