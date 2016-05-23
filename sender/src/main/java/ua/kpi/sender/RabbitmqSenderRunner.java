package ua.kpi.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import ua.kpi.RoutingInfo;

@SpringBootApplication
public class RabbitmqSenderRunner implements CommandLineRunner {

    private static final int TYPE_1_MESSAGES_PER_SECOND = 2;
    private static final int TYPE_2_MESSAGES_PER_SECOND = 1;
    private static final int TYPE_3_MESSAGES_PER_SECOND = 5;

    @Autowired
    AbstractApplicationContext context;

    @Autowired
    Sender sender;

    @Bean
    public Sender sender() {
        return new Sender();
    }

    @Override
    public void run(String... args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sender.sendShutdownSignal();
        }));
        while (!sender.isShutdownSignalReceived()) {
            sender.send(TYPE_1_MESSAGES_PER_SECOND, RoutingInfo.TYPE_1);
            sender.send(TYPE_2_MESSAGES_PER_SECOND, RoutingInfo.TYPE_2);
            sender.send(TYPE_3_MESSAGES_PER_SECOND, RoutingInfo.TYPE_3);
            Thread.sleep(1000);
        }
        context.close();
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqSenderRunner.class, args);
    }
}
