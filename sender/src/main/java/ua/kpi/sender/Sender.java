package ua.kpi.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ua.kpi.RoutingInfo;

public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private boolean shutdownSignalReceived;

    public void send(int messagesNumber, RoutingInfo routingInfo) {
        for (int i = 1; i <= messagesNumber; i++) {
            final String formattedMessage = String.format("[%s message #%s]: Test message", routingInfo.name(), i);
            System.out.format("Sending %s message: '%s'%n", routingInfo.name(), formattedMessage);
            this.rabbitTemplate.convertAndSend(routingInfo.getDestination(), formattedMessage);
        }
    }

    public void sendShutdownSignal() {
        this.shutdownSignalReceived = true;
    }

    public boolean isShutdownSignalReceived() {
        return this.shutdownSignalReceived;
    }
}
