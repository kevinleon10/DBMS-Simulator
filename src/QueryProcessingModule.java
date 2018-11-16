import java.util.ArrayDeque;
import java.util.Random;
import java.util.Vector;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;

public class QueryProcessingModule extends Module {

    /**
     * Constructor for QueryProcessingModule
     * @param totalServers number of servers
     * @param rnd random
     */
    QueryProcessingModule(int totalServers, Random rnd) {
        queryQueue = new ArrayDeque<>();
        this.totalServers = totalServers;
        this.rnd = rnd;
        this.idle = true;
        this.idleTime = 0;
        this.moduleType = ModuleType.QUERY_PROCESSING_MODULE;
        this.moduleStatistics = new ModuleStatistics(this.moduleType);
    }

    /**
     * Processes the arrival of a query to the module
     * @param eventList list of system events
     * @param query incoming query
     * @param clock system actual time
     * @return the events list
     */
    public Vector<Event> processArrival(Vector<Event> eventList, Query query, double clock) {
        moduleStatistics.increaseArrivedClients();
        if (idle) {
            moduleStatistics.increaseModuleIdleTime(idleTime, clock);
            idleTime = 0;
            idle = false;
        }
        query.setModuleArrivalTime(clock);
        query.setActualModule(this.moduleType);
        if (this.totalServers > this.busyServers) {
            this.busyServers++;
            eventList = this.processQuery(eventList, query, clock);
        } else {
            this.queryQueue.add(query);
            moduleStatistics.increaseQueueSize();
        }
        return eventList;
    }

    /**
     * Processes the query
     * @param eventList list of system events
     * @param query query to be attended
     * @param clock system actual time
     * @return the events list
     */
    public Vector<Event> processQuery(Vector<Event> eventList, Query query, double clock) {
        double serviceTime = generateServiceTime(query);
        Event newEvent = new Event(EventType.QUERY_PROCESSING_MODULE_DEPARTURE, query, serviceTime + clock);
        eventList.add(newEvent);
        return eventList;
    }

    /**
     * Processes the departure of a query from the module
     * @param eventList list of system events
     * @param query outgoing query
     * @param clock system actual time
     * @return the events list
     */
    public Vector<Event> processDeparture(Vector<Event> eventList, Query query, double clock) {
        moduleStatistics.increaseTotalTimeInModule(query.getTypeInteger(), query.getModuleArrivalTime(), clock);
        if (queryQueue.isEmpty()) {
            busyServers--;
            if (busyServers == 0) {
                idleTime = clock;
                idle = true;
            }
        } else {
            moduleStatistics.decreaseQueueSize();
            moduleStatistics.increaseTotalQueueTime(queryQueue.peek().getModuleArrivalTime(), clock);
            eventList = processQuery(eventList, queryQueue.poll(), clock);
        }
        moduleStatistics.increaseServedClients(query.getTypeInteger());
        return eventList;
    }

    /**
     * Generates the service time for a query
     * @param query query to be processed
     * @return the service time
     */
    private double generateServiceTime(Query query) {

        double serviceTime = 0;
        if (rnd.nextDouble() < 0.70) {
            serviceTime += (1 / 10);
        } else {
            serviceTime += (4 / 10);
        }

        serviceTime += 0.8 * rnd.nextDouble(); // Even
        serviceTime += (rnd.nextGaussian() * sqrt(0.5)) + 1; //Normal
        serviceTime += -0.7 * log(rnd.nextDouble()); // Exponential

        if (query.isReadOnly()) {
            serviceTime += 0.1;
        } else {
            serviceTime += 1 / 2;
        }

        return serviceTime;
    }

    /**
     * Removes a query from the queue
     * @param query query to be removed
     */
    public void removeQueryFromQueue(Query query) {
        if (!this.queryQueue.isEmpty() && this.queryQueue.contains(query)) {
            this.queryQueue.remove(query);
        }
    }
}