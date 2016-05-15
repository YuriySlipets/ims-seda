package ua.kpi;

public enum RoutingInfo {

    TYPE_1("exchange_1", "destination_1", "key_1"),
    TYPE_2("exchange_2", "destination_2", "key_2"),
    TYPE_3("exchange_3", "destination_3", "key_3");


    private final String exchange;
    private final String destination;
    private final String key;

    RoutingInfo(String exchange, String destination, String key) {
        this.exchange = exchange;
        this.destination = destination;
        this.key = key;
    }

    public String getExchange() {
        return exchange;
    }

    public String getDestination() {
        return destination;
    }

    public String getKey() {
        return key;
    }
}
