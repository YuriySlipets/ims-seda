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

    @Autowired
    AbstractApplicationContext context;

    @Bean
    public Sender sender() {
        return new Sender();
    }

    @Override
    public void run(String... args) throws Exception {
        sender().send(7, RoutingInfo.TYPE_1);
        sender().send(10, RoutingInfo.TYPE_2);
        sender().send(23, RoutingInfo.TYPE_3);
        context.close();
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqSenderRunner.class, args);
    }
}
