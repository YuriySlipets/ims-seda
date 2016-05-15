package ua.kpi;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SedaControllerRunner {

    public static void main(String[] args) throws InterruptedException {
        final ArrayList<ReceiverRunner> receiverRunners = new ArrayList<>();

        receiverRunners.add(new ReceiverRunner(RoutingInfo.TYPE_1));
        receiverRunners.add(new ReceiverRunner(RoutingInfo.TYPE_2));
        receiverRunners.add(new ReceiverRunner(RoutingInfo.TYPE_3));
        receiverRunners.add(new ReceiverRunner(RoutingInfo.TYPE_3));
        receiverRunners.add(new ReceiverRunner(RoutingInfo.TYPE_3));

        final ExecutorService executorService = Executors.newFixedThreadPool(receiverRunners.size());
        receiverRunners.forEach(executorService::execute);
    }
}
