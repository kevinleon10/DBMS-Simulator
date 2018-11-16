import java.util.ArrayDeque;
import java.util.Random;
import java.util.Vector;
import static java.lang.Math.sqrt;

public class ProcessModule extends Module {

    /**
     * Constructor for ProcessModule
     * @param rnd random
     */
    ProcessModule(Random rnd) {
        this.queryQueue = new ArrayDeque<>();
        this.totalServers = 1;
        this.rnd = rnd;
        this.busyServers = 0;
        this.idle = true;
        this.idleTime = 0;
        this.moduleType = ModuleType.PROCESS_MODULE;
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
        if (this.busyServers == 0) {
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
        double serviceTime = (rnd.nextGaussian() * sqrt(0.1)) + 1.5;
        Event newEvent = new Event(EventType.PROCESS_MODULE_DEPARTURE, query, serviceTime + clock);
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
     * Removes a query from the queue
     * @param query query to be removed
     */
    public void removeQueryFromQueue(Query query) {
        if (!this.queryQueue.isEmpty() && this.queryQueue.contains(query)) {
            this.queryQueue.remove(query);
        }
    }
}