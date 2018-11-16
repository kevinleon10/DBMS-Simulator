import java.util.Random;

public class Query implements Comparable<Query>{
    private double systemArrivalTime; //Momento en que llega la consulta al sistema.
    private double moduleArrivalTime; //Momento en que llega la consulta al respectivo módulo.
    private int queryID; //Identificación única de cada consulta.
    private long neededDiskBlocks;
    private QueryType queryType; //El tipo de consulta realizada.
    private Random rnd;
    private ModuleType actualModule;

    /**
     * Constructor for Query
     * @param systemArrivalTime time when the query arrived to the system
     * @param rnd Random
     */
    public Query(double systemArrivalTime, Random rnd) {
        this.systemArrivalTime = systemArrivalTime;
        this.moduleArrivalTime = 0;
        this.neededDiskBlocks = 0;
        this.rnd = rnd;
        this.queryID = this.rnd.nextInt();
        this.generateType(); //Se asigna el tipo de consulta
        this.actualModule = null;
    }

    /**
     * Compares two queries
     * @param secondQuery second query to be compared
     * @return the difference of the priorities
     */
    @Override
    public int compareTo(Query secondQuery){
        return (int)(this.getTypeInteger() - secondQuery.getTypeInteger());
    }

    /**
     * Returns the time when a query arrived to the system
     * @return the time when a query arrived to the system
     */
    public double getSystemArrivalTime() {
        return systemArrivalTime;
    }

    /**
     * Sets the time when the query arrived to the module
     * @param moduleArrivalTime time when the query arrived to a module
     */
    public void setModuleArrivalTime(double moduleArrivalTime) {
        this.moduleArrivalTime = moduleArrivalTime;
    }

    /**
     * Returns the time when the query arrived to the module
     * @return the time when the query arrived to a module
     */
    public double getModuleArrivalTime() {
        return moduleArrivalTime;
    }

    /**
     * Get the number of needed disk blocks
     * @return the number of needed disk blocks
     */
    public long getNeededDiskBlocks() {
        return neededDiskBlocks;
    }

    /**
     * Sets the number of needed disk blocks
     * @param neededDiskBlocks the number of needed blocks
     */
    public void setNeededDiskBlocks(long neededDiskBlocks) {
        this.neededDiskBlocks = neededDiskBlocks;
    }

    /**
     * Generates the type of query
     */
    private void generateType() {
        double randNum = rnd.nextDouble(); // Convierte el random en un número entre 0 y 1.
        if (randNum < 0.32) {  //El caso en que el tipo de consulta sea SELECT.
            queryType = QueryType.SELECT;
        }
        else if (randNum < 0.60) { //El caso en que el tipo de consulta sea UPDATE.
            queryType = QueryType.UPDATE;
        }
        else if (randNum < 0.93) { //El caso en que el tipo de consulta sea JOIN.
            queryType = QueryType.JOIN;
        }
        else { //El caso en que el tipo de consulta sea DDL.
            queryType = QueryType.DDL;
        }
    }

    /**
     * Returns the query ID
     * @return the query ID
     */
    public int getQueryID() {
        return queryID;
    }

    /**
     * Returns the query type as an integer by its priority
     * @return the query type as an integer by its priority
     */
    public int getTypeInteger() {
        return queryType.getPriority();
    }

    /**
     * Returns true if the query if read only, otherwise, it returns false
     * @return true if the query if read only, otherwise, it returns false
     */
    public boolean isReadOnly(){
       return queryType.isReadOnly();
    }

    /**
     * Returns the current module where the query is located
     * @return the current module where the query is located
     */
    public ModuleType getActualModule() {
        return actualModule;
    }

    /**
     * Sets the current module where the query is located
     * @param actualModule the current module where the query is located
     */
    public void setActualModule(ModuleType actualModule) {
        this.actualModule = actualModule;
    }
}