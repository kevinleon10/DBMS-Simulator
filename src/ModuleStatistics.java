public class ModuleStatistics {

    private int actualQueueSize;
    private double arrivalRate;
    private double totalTimeInQueue; // Wq
    private double totalTimeInModule; // W
    private double totalTimeInModuleDDL; // DDL W
    private double totalTimeInModuleUPDATE; // UPDATE W
    private double totalTimeInModuleJOIN; // JOIN W
    private double totalTimeInModuleSELECT; // SELECT W
    private double moduleIdleTime; // t*(1-p)
     private double servedClients;
    private double arrivedClients;
    private double totalDDLQuery; // Served DDLs
    private double totalUPDATEQuery; // Served UPDATEs
    private double totalJOINQuery; // Served JOINs
    private double totalSELECTQuery; // Served SELECTs
    private ModuleType moduleType;

    /**
     * Constructor for ModuleStatistics
     */
    public ModuleStatistics(ModuleType moduleType) {
        this.arrivalRate = 0;
        this.actualQueueSize = 0;
        this.totalTimeInQueue = 0;
        this.totalTimeInModule = 0;
        this.totalTimeInModuleDDL = 0;
        this.totalTimeInModuleUPDATE = 0;
        this.totalTimeInModuleJOIN = 0;
        this.totalTimeInModuleSELECT = 0;
        this.moduleIdleTime = 0;
        this.servedClients = 0;
        this.arrivedClients = 0;
        this.totalDDLQuery = 0;
        this.totalUPDATEQuery = 0;
        this.totalJOINQuery = 0;
        this.totalSELECTQuery = 0;
        this.moduleType = moduleType;
    }

    /**
     * Adds 1 to queue size
     */
    public void increaseQueueSize(){
        actualQueueSize += 1;
    }

    /**
     * Substract 1 to queue size
     */
    public void decreaseQueueSize(){
        actualQueueSize -= 1;
    }

    /**
     * Adds 1 to served clients and adds 1 to served clients for an specific type of client
     */
    public void increaseServedClients(int sentence) {
        switch (sentence) {
            case 1: // If DDL
                totalDDLQuery += 1.0;
                break;
            case 2: // If UPDATE
                totalUPDATEQuery += 1.0;
                break;
            case 3: //If JOIN
                totalJOINQuery += 1.0;
                break;
            case 4: // If SELECT
                totalSELECTQuery += 1.0;
                break;
        }
        servedClients++;
    }

    /**
     * Increases total time in module
     * @param sentence type of query
     * @param moduleArrivalTime query arrival time
     * @param moduleDepartureTime query departure time
     */
    public void increaseTotalTimeInModule(int sentence, double moduleArrivalTime, double moduleDepartureTime) {
        switch (sentence) {
            case 1: // If DDL
                totalTimeInModuleDDL += (moduleDepartureTime - moduleArrivalTime);
                break;
            case 2: // If UPDATE
                totalTimeInModuleUPDATE += (moduleDepartureTime - moduleArrivalTime);
                break;
            case 3: // If JOIN
                totalTimeInModuleJOIN += (moduleDepartureTime - moduleArrivalTime);
                break;
            case 4: // If SELECT
                totalTimeInModuleSELECT += (moduleDepartureTime - moduleArrivalTime);
                break;
        }
        totalTimeInModule += (moduleDepartureTime - moduleArrivalTime);
    }

    /**
     * Increses total time in queue
     * @param queueArrivalTime query arrival time to module's queue
     * @param serviceTime query's time of service
     */
    public void increaseTotalQueueTime(double queueArrivalTime, double serviceTime) {
        totalTimeInQueue += (serviceTime - queueArrivalTime);
    }

    /**
     * Increses module idle time
     * @param initialIdleTime time when module starts to be unoccupied
     * @param finishIdleTime time when module stops to be unoccupied
     */
    public void increaseModuleIdleTime(double initialIdleTime, double finishIdleTime) {
        moduleIdleTime += (finishIdleTime - initialIdleTime);
    }

    /**
     * Increases arrived clients
     */
    public void increaseArrivedClients() {
        arrivedClients++;
    }

    /**
     * Returns average time in module per sentence
     * @param sentence type of query
     * @return average time in module per sentence
     */
    public double getAverageTimeInModulePerSentence(int sentence) {
        double average = 0;
        switch (sentence) {
            case 1: // If DDL
                if (totalDDLQuery > 0) {
                    average = totalTimeInModuleDDL / totalDDLQuery;
                }
                break;
            case 2: // If UPDATE
                if (totalUPDATEQuery > 0) {
                    average = totalTimeInModuleUPDATE / totalUPDATEQuery;
                }
                break;
            case 3: // If JOIN
                if (totalJOINQuery > 0) {
                    average = totalTimeInModuleJOIN / totalJOINQuery;
                }
                break;
            case 4: // If SELECT
                if (totalSELECTQuery > 0) {
                    average = totalTimeInModuleSELECT / totalSELECTQuery;
                }
                break;
        }
        return average;
    }

    /**
     * Returns Average time in module
     * @return average time in module
     */
    public double getAverageTimeInModule(){
        double average = 0;
        if (servedClients > 0) {
        average = totalTimeInModule / servedClients;
        }
        return average;
    }

    /**
     * Returns average queue time
     * @return average queue time
     */
    public double getAverageQueueTime(){
        double average = 0;
        if (servedClients > 0) {
            average = totalTimeInQueue / servedClients;
        }
        return average;
    }

    /**
     * Returns average service time
     * @return average service time
     */
    public double getAverageServiceTime() {
        double average = totalTimeInModule - totalTimeInQueue;
        if (average > 0) {
            average = average/servedClients;
        }
        return  average;
    }

    /**
     * Returns average queue size
     * @return average queue size
     */
    public double generateAverageQueueSize() {
        return arrivalRate * this.getAverageQueueTime();
    }

    /**
     * Returns average number of clients in module
     * @return average numner of clients in module
     */
    public double generateAverageClientsInModule() {
        return arrivalRate * this.getAverageTimeInModule();
    }

    /**
     * Get average numbers of clients in service
     * @return average numbers of clients in service
     */
    public double getAverageClientInService(){
        return this.generateAverageClientsInModule() - this.generateAverageQueueSize();
    }

    /**
     * Returns module idle time
     * @return module idle time
     */
    public double getIdleTime(){
        return moduleIdleTime;
    }

    /**
     * Returns arrival rate to module
     * @param clock system clock
     * @return arrival rate to module
     */
    public double getArrivalRate(double clock) {
            arrivalRate = arrivedClients/clock;
            return arrivalRate;
    }

    /**
     * Returns the module departure rate
     * @return the module departure rate
     */
    public double generateDepartureRate() {
        double average = this.getAverageServiceTime();
        if (average >0) {
            average = 1/average;
        }
        return average;
    }

    /**
     * Returns module served clients
     * @return module served clients
     */
    public double getServedClients() {
        return servedClients;
    }

    /**
     * Returns actual queue size
     * @return actual queue size
     */
    public int getActualQueueSize() {
        return actualQueueSize;
    }

    /**
     * Returns module type
     * @return module type
     */
    public ModuleType getModuleType() {
        return moduleType;
    }

    public void setAverageTimeInModulePerSentence(double totalTimeInModulePerSentence, int sentence) {
        switch (sentence) {
            case 1: // If DDL
               totalTimeInModuleDDL += totalTimeInModulePerSentence;
                break;
            case 2: // If UPDATE
                totalTimeInModuleUPDATE += totalTimeInModulePerSentence;
                break;
            case 3: // If JOIN
                totalTimeInModuleJOIN += totalTimeInModulePerSentence;
                break;
            case 4: // If SELECT
                totalTimeInModuleSELECT += totalTimeInModulePerSentence;
                break;
        }
    }

    /**
     * Sets average module idle time for all runs
     * @param moduleIdleTime module idle time
     * @param counter number of runs
     */
    public void setModuleIdleTime(double moduleIdleTime, int counter) {
        this.moduleIdleTime += (moduleIdleTime/counter);
    }

    /**
     * Sets total arrivals per sentence
     * @param totalSentenceQuery total arrival per sentence
     */
    public void setTotalSentenceQuery(int totalSentenceQuery){
        this.totalDDLQuery = 1.0*totalSentenceQuery;
        this.totalUPDATEQuery = 1.0*totalSentenceQuery;
        this.totalJOINQuery = 1.0*totalSentenceQuery;
        this.totalSELECTQuery = 1.0*totalSentenceQuery;
    }

    /**
     * Set served clients
     * @param servedClients server clients per module
     */
    public void setServedClients(int servedClients){
        this.servedClients = servedClients;
    }

    /**
     * Set arrived clients
     * @param arrivedClients arrived clients per module
     */
    public void setArrivedClients(double arrivedClients){
        this.arrivedClients += arrivedClients;
    }

    /**
     * Set total time in Queue
     * @param totalTimeInQueue total time in queue
     */
    public void setTotalTimeInQueue(double totalTimeInQueue) {
        this.totalTimeInQueue += totalTimeInQueue;
    }

    /**
     * Set total time in Module
     * @param totalTimeInMdoule total time in module
     */
    public void setTotalTimeInModule(double totalTimeInModule) {
        this.totalTimeInModule += totalTimeInModule;
    }
}