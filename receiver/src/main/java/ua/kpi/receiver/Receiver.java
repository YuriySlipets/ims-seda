package ua.kpi.receiver;

import ua.kpi.RoutingInfo;

public class Receiver implements Runnable {

    private static final int HEARTBEAT_INTERVAL_MILLIS = 1000;
    private final RoutingInfo routingInfo;
    private volatile Boolean isIdle = true;
    private volatile boolean isShutdownSignalReceived = false;

    public Receiver(RoutingInfo routingInfo) {
        this.routingInfo = routingInfo;
    }

    public void receiveMessage(final String message) throws InterruptedException {
        isIdle = false;

        System.out.format("Received message from '%s' destination: %s%n", routingInfo.getDestination(), message);
        System.out.println("Some time-consuming processing...");
        Thread.sleep(2000);
        System.out.println("Successfully finished message processing!");

        isIdle = true;
    }

    @Override
    public void run() {
        while (!isShutdownSignalReceived) {
            try {
                if (isIdle) {
                    System.out.format("[Heartbeat]: %s receiver is alive%n", routingInfo.getDestination());
                    Thread.sleep(HEARTBEAT_INTERVAL_MILLIS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendShutdownSignal() {
        this.isShutdownSignalReceived = true;
    }
}
