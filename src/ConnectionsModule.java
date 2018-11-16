import java.util.Random;
import java.util.Vector;
import static java.lang.Math.log;

public class ConnectionsModule extends Module {

    private double timeOut; // Maximum connection lifetime
    private QueryStatistics queryStatistics; // Statistics for queries

    /**
     * Constructor for ConnectionsModule
     * @param totalServers number of servers
     * @param timeOut maximum connection lifetime
     * @param rnd random
     * @param queryStatistics statistics for queries
     */
    ConnectionsModule(int totalServers, double timeOut, Random rnd, QueryStatistics queryStatistics) {
        this.totalServers = totalServers;
        this.timeOut = timeOut;
        this.rnd = rnd;
        this.queryStatistics = queryStatistics;
        this.busyServers = 0;
        this.idleTime = 0;
        this.moduleType = ModuleType.CONNECTION_MODULE;
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
        if (this.totalServers > this.busyServers) {
            query.setActualModule(this.moduleType);
            this.busyServers++;
            this.queryStatistics.increaseTotalConnections();
            Event newEvent = new Event(EventType.KILL_QUERY, query, this.timeOut+clock);
            eventList.add(newEvent);
            eventList = this.processQuery(eventList, query, clock);
        } else {
            this.queryStatistics.increaseDiscardedConnections();
        }
        double arrivalTime = (-60*log(rnd.nextDouble())/35); // Generates next arrival time from an exponential distribution
        Event newEvent = new Event(EventType.CONNECTIONS_MODULE_ARRIVAL, new Query(arrivalTime+clock, rnd), arrivalTime + clock);
        eventList.add(newEvent);
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
        double serviceTime = 0.01 + (0.04) * rnd.nextDouble(); // Generates service time from an even distribution
        Event newEvent = new Event(EventType.PROCESS_MODULE_ARRIVAL, query, serviceTime + clock);
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
        int index = 0;
        boolean hasBeenRemoved = false;
        double connectionSystemTime = clock - query.getModuleArrivalTime();
        this.busyServers --;
        this.queryStatistics.increaseAverageConnectionLifeTime(connectionSystemTime);
        while(index<eventList.size() && !hasBeenRemoved){
            if(eventList.get(index).getQueryReference() == query){
                eventList.remove(index);
                hasBeenRemoved = true;
            }
            index++;
        }
        moduleStatistics.increaseServedClients(query.getTypeInteger());
        moduleStatistics.increaseTotalTimeInModule(query.getTypeInteger(),query.getSystemArrivalTime(),clock);
        return eventList;
    }

    /**
     * Returns the query statistics
     * @return query statistics
     */
    public QueryStatistics getQueryStatistics() {
        return queryStatistics;
    }
}