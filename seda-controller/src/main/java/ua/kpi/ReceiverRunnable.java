package ua.kpi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReceiverRunnable implements Runnable {

    private volatile Boolean shutdownHookReceived;
    private final RoutingInfo messageType;

    public ReceiverRunnable(final RoutingInfo messageType) {
        this.shutdownHookReceived = false;
        this.messageType = messageType;
    }

    @Override
    public void run() {
        try {
            final Runtime runTime = Runtime.getRuntime();
            final Process process = runTime.exec("java -DmessageType=" + this.messageType.name() + " -jar receiver/target/receiver-1.0-SNAPSHOT.jar");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while (!shutdownHookReceived) {
                String line = bufferedReader.readLine();
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendShutdownHook() {
        this.shutdownHookReceived = true;
    }
}
