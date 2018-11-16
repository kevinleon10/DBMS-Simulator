import java.util.*;

public abstract class Module {

    protected Queue<Query> queryQueue; //Query queue
    protected ModuleType moduleType; //Module type
    protected Random rnd; //Random
    protected int totalServers; //Total number os servers
    protected int busyServers; //Número de servidores ocupados
    protected ModuleStatistics moduleStatistics; // Estadisticas basicas de para cada modulo
    protected boolean idle; // Referencia si el modulo esta ocioso o no
    protected double idleTime; // Tiempo en que un modulo empieza a estar desocupado

    /**
     * Processes the arrival of a query to the module
     * @param eventList list of system events
     * @param query incoming query
     * @param clock system actual time
     * @return the events list
     */
    public abstract Vector<Event> processArrival(Vector<Event> eventList, Query query, double clock);

    /**
     * Processes the query
     * @param eventList list of system events
     * @param query query to be attended
     * @param clock system actual time
     * @return the events list
     */
    protected abstract Vector<Event> processQuery(Vector<Event> eventList, Query query, double clock);

    /**
     * Returns the number of busy servers
     * @return number of busy servers
     */
    public int getBusyServers() {
        return busyServers;
    }

    /**
     * Returns the statistics of the module
     * @return the statistics of the module
     */
    public ModuleStatistics getModuleStatistics() {
        return moduleStatistics;
    }

    /**
     * Returns available servers
     * @return number of avialable servers
     */
    public int getAvailableServers(){
        return (this.totalServers-this.busyServers);
    }
}