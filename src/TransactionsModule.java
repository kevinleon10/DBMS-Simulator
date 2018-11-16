import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;
import static java.lang.Math.pow;
import static java.lang.Math.round;

public class TransactionsModule extends Module {

    private int loadedBlocks;
    private boolean attendingDDL;

    /**
     * Constructor for QueryProcessingModule
     * @param totalServers number of servers
     * @param rnd random
     */
    TransactionsModule(int totalServers, Random rnd) {
        queryQueue = new PriorityQueue<>();
        this.totalServers = totalServers;
        this.busyServers = 0;
        this.loadedBlocks = 0;
        this.attendingDDL = false;
        this.rnd = rnd;
        this.idle = true;
        this.idleTime = 0;
        this.moduleType = ModuleType.TRANSACTIONS_MODULE;
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
            if (query.getTypeInteger() == 1) {  // If query type is DDL
                if (busyServers == 0) {
                    this.busyServers++;
                    attendingDDL = true;
                    eventList = this.processQuery(eventList, query, clock);
                } else {
                    queryQueue.add(query);
                    moduleStatistics.increaseQueueSize();
                }
            } else {
                if (attendingDDL) {
                    queryQueue.add(query);
                    moduleStatistics.increaseQueueSize();
                } else {
                    this.busyServers++;
                    eventList = this.processQuery(eventList, query, clock);
                }
            }
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
        query.setNeededDiskBlocks(loadedBlocks);
        Event newEvent = new Event(EventType.TRANSACTIONS_MODULE_DEPARTURE, query, serviceTime + clock);
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
        attendingDDL = false;
        if (queryQueue.isEmpty()) {
            busyServers--;
            if (busyServers == 0) {
                idleTime = clock;
                idle = true;
            }
        } else {
            if (queryQueue.peek().getTypeInteger() == 1) { // If the first query is DDL
                if (busyServers == 1) { //If there was only one busy server
                    attendingDDL = true;
                    moduleStatistics.decreaseQueueSize();
                    moduleStatistics.increaseTotalQueueTime(queryQueue.peek().getModuleArrivalTime(), clock);
                    eventList = this.processQuery(eventList, queryQueue.poll(), clock);
                }
            } else {
                moduleStatistics.decreaseQueueSize();
                moduleStatistics.increaseTotalQueueTime(queryQueue.peek().getModuleArrivalTime(), clock);
                eventList = this.processQuery(eventList, queryQueue.poll(), clock);
            }
        }
        moduleStatistics.increaseServedClients(query.getTypeInteger());
        return eventList;
    }

    /**
     * Returns the number of loaded disk blocks
     * @return the number of loaded disk blocks
     */
    public int getLoadedBlocks() {
        return loadedBlocks;
    }

    /**
     * Generates the service time for a query
     * @param query query to be processed
     * @return the service time
     */
    private double generateServiceTime(Query query) {
        double serviceTime = totalServers * 0.03;

        switch (query.getTypeInteger()) {
            case 1:
                loadedBlocks = 0;
                break;
            case 2:
                loadedBlocks = 0;
                break;
            case 3:
                loadedBlocks = ((1 + round(15 * rnd.nextFloat())) + (1 + round(11 * rnd.nextFloat()))); // Even distribution
                break;
            case 4:
                loadedBlocks = (1 + round(63 * rnd.nextFloat())); // Even distribution
                break;
        }
        serviceTime += loadedBlocks/10;
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