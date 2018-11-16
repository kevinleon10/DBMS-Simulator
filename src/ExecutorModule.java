import java.util.ArrayDeque;
import java.util.Vector;

import static java.lang.Math.pow;

public class ExecutorModule extends Module {

    /**
     * Constructor for ExecutorModule
     * @param totalServers number of servers
     */
    ExecutorModule(int totalServers) {
        queryQueue = new ArrayDeque<>();
        this.totalServers = totalServers;
        this.idleTime = 0;
        this.moduleType = ModuleType.EXECUTOR_MODULE;
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
        Event newEvent = new Event(EventType.SYSTEM_DEPARTURE, query, serviceTime + clock);
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
        switch (query.getTypeInteger()) {
            case 1: // If DDL
                serviceTime += 1/2;
                break;
            case 2: // If UPDATE
                serviceTime += 1;
                break;
            default:
                serviceTime += pow(query.getNeededDiskBlocks(), 2) / 1000;
                serviceTime += query.getNeededDiskBlocks() / 6;
                break;
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