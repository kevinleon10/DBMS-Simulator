import java.util.Random;
import java.util.Vector;

public class Simulation {

    private double clock; // System clock
    private Vector<Event> eventList; // Vector that contains the events
    private double timeOut; // Maximum query lifetime
    private Random rnd; // Random
    private QueryStatistics queryStatistics; // Query statistics
    private ExecutorModule executorModule; // Executor module
    private TransactionsModule transactionsModule; // Transactions module
    private QueryProcessingModule queryProcessingModule; // Query processing module
    private ProcessModule processModule; // Process module
    private ConnectionsModule connectionsModule; // Connections module
    private Event event;
    private int totalKilledQueries;

    /**
     * Constructor for Simulation
     * @param k number of maximum simultaneous connections
     * @param t maximum query lifetime
     * @param n query processing module's number of servers
     * @param p transactions module's number of servers
     * @param m executor module's number of servers
     */
    public Simulation(int k, double t, int n, int p, int m) {
        this.clock = 0;
        this.eventList = new Vector<>();
        this.rnd = new Random();
        this.queryStatistics = new QueryStatistics();
        this.executorModule = new ExecutorModule(m);
        this.transactionsModule = new TransactionsModule(p, rnd);
        this.queryProcessingModule = new QueryProcessingModule(n, rnd);
        this.processModule = new ProcessModule(rnd);
        this.connectionsModule = new ConnectionsModule(k, t, rnd, queryStatistics);
        this.timeOut = t;
        this.event = new Event();
        this.totalKilledQueries = 0;
    }

    /**
     * Processes the next event in the events vector
     */
    public void processEvent() {
        this.event = this.getNextEvent();
        this.clock = event.getEventTime();

        switch (event.getEventType()) {
            case CONNECTIONS_MODULE_ARRIVAL:
                eventList = connectionsModule.processArrival(eventList, event.getQueryReference(), clock);
                break;
            case PROCESS_MODULE_ARRIVAL:
                eventList = processModule.processArrival(eventList, event.getQueryReference(), clock);
                break;
            case PROCESS_MODULE_DEPARTURE:
                eventList = processModule.processDeparture(eventList, event.getQueryReference(), clock);
                eventList = queryProcessingModule.processArrival(eventList, event.getQueryReference(), clock);
                break;
            case QUERY_PROCESSING_MODULE_DEPARTURE:
                eventList = queryProcessingModule.processDeparture(eventList, event.getQueryReference(), clock);
                eventList = transactionsModule.processArrival(eventList, event.getQueryReference(), clock);
                break;
            case TRANSACTIONS_MODULE_DEPARTURE:
                eventList = transactionsModule.processDeparture(eventList, event.getQueryReference(), clock);
                eventList = executorModule.processArrival(eventList, event.getQueryReference(), clock);
                break;
            case SYSTEM_DEPARTURE:
                eventList = executorModule.processDeparture(eventList, event.getQueryReference(), clock);
                eventList = connectionsModule.processDeparture(eventList, event.getQueryReference(), clock);
                break;
            case KILL_QUERY:
                killQuery(event);
                break;
            case CONNECTIONS_MODULE_DEPARTURE:
                eventList = processKilledQueryDepartureFromCurrentModule(eventList, event.getQueryReference(), clock);
                eventList = connectionsModule.processDeparture(eventList, event.getQueryReference(), clock);
                break;
            default:
                break;
        }
    }

    /**
     * Generates the first arrival to the system
     */
    public void generateFirstArrival() {
        Event event = new Event(EventType.CONNECTIONS_MODULE_ARRIVAL, new Query(0, rnd), 0);
        eventList.add(event);
    }

    /**
     * Gets the next event in time to be executed
     * @return the next event in time
     */
    private Event getNextEvent() {
        int minEventIndex = 0;
        Event minEvent = eventList.firstElement();

        for (int index = 0; index < eventList.size(); index++) {
            if (eventList.get(index).getEventTime() < minEvent.getEventTime()) {
                minEvent = eventList.get(index);
                minEventIndex = index;
            } else if (eventList.get(index).getEventTime() == minEvent.getEventTime()) {
                if (eventList.get(index).getEventType() == EventType.KILL_QUERY) {
                    minEvent = eventList.get(index);
                    minEventIndex = index;
                }
            }
        }
        eventList.remove(minEventIndex);
        return minEvent;
    }

    /**
     * Finds and remove the event associated with the expired query
     * Otherwise, it removes the query from the module's queue
     * @param event the kill event to be processed
     */
    private void killQuery(Event event) {
        Query query = event.getQueryReference();
        boolean hasBeenRemoved = false;
        int index = 0;
        double serviceEndTime = 0;
        while (index < eventList.size() && !hasBeenRemoved) {
            if (eventList.get(index).getQueryReference().getQueryID() == query.getQueryID()) {
                if (eventList.get(index).getEventType() != EventType.SYSTEM_DEPARTURE) {
                    ++totalKilledQueries;
                    serviceEndTime = eventList.get(index).getEventTime();
                    eventList.remove(index);
                    hasBeenRemoved = true;
                    Event newEvent = new Event(EventType.CONNECTIONS_MODULE_DEPARTURE, query, serviceEndTime);
                    eventList.add(newEvent);
                }
            }
            index++;
        }
        if (!hasBeenRemoved) {
            processModule.removeQueryFromQueue(event.getQueryReference());
            queryProcessingModule.removeQueryFromQueue(event.getQueryReference());
            transactionsModule.removeQueryFromQueue(event.getQueryReference());
            executorModule.removeQueryFromQueue(event.getQueryReference());
        }
    }

    /**
     * Processes the departure of a killed query from the module where it is
     * @param eventList   list of system events
     * @param killedQuery the query that has been killed
     * @param clock       system actual time
     * @return the events list
     */
    public Vector<Event> processKilledQueryDepartureFromCurrentModule(Vector<Event> eventList, Query killedQuery, double clock) {
        switch (killedQuery.getActualModule()) {
            case PROCESS_MODULE:
                eventList = processModule.processDeparture(eventList, killedQuery, clock);
                break;
            case QUERY_PROCESSING_MODULE:
                eventList = queryProcessingModule.processDeparture(eventList, killedQuery, clock);
                break;
            case TRANSACTIONS_MODULE:
                eventList = transactionsModule.processDeparture(eventList, killedQuery, clock);
                break;
            case EXECUTOR_MODULE:
                eventList = executorModule.processDeparture(eventList, killedQuery, clock);
                break;
        }
        return eventList;
    }

    /**
     * Returns the actual event
     * @return the actual event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Returns system clock
     * @return system clock
     */
    public double getClock() {
        return clock;
    }

    /**
     * Returns the query statistics
     * @return query statistics
     */
    public QueryStatistics getQueryStatistics() {
        return connectionsModule.getQueryStatistics();
    }

    /**
     * Returns the executor module
     * @return executor module
     */
    public ExecutorModule getExecutorModule() {
        return executorModule;
    }

    /**
     * Returns the transactions module
     * @return the transactions module
     */
    public TransactionsModule getTransactionsModule() {
        return transactionsModule;
    }

    /**
     * Returns the query processing module
     * @return the query processing module
     */
    public QueryProcessingModule getQueryProcessingModule() {
        return queryProcessingModule;
    }

    /**
     * Returns the process module
     * @return the process module
     */
    public ProcessModule getProcessModule() {
        return processModule;
    }

    /**
     * Returns the connections module
     * @return the connections module
     */
    public ConnectionsModule getConnectionsModule() {
        return connectionsModule;
    }

    public int getTotalKilledQueries() {
        return totalKilledQueries;
    }
}