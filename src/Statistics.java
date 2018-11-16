
public class Statistics {

    private QueryStatistics queryStatistics;
    private ModuleStatistics connectionsModuleStatistics;
    private ModuleStatistics processModuleStatistics;
    private ModuleStatistics queryProcessingStatistics;
    private ModuleStatistics transactionsModuleStatistics;
    private ModuleStatistics executorModuleStatistics;
    private double finalClock;

    /**
     * Empty constructor for Statistics;
     */
    Statistics() {
        this.queryStatistics = new QueryStatistics();
        this.connectionsModuleStatistics = new ModuleStatistics(ModuleType.CONNECTION_MODULE);
        this.processModuleStatistics = new ModuleStatistics(ModuleType.PROCESS_MODULE);
        this.queryProcessingStatistics = new ModuleStatistics(ModuleType.QUERY_PROCESSING_MODULE);
        this.transactionsModuleStatistics = new ModuleStatistics(ModuleType.TRANSACTIONS_MODULE);
        this.executorModuleStatistics = new ModuleStatistics(ModuleType.EXECUTOR_MODULE);
        this.finalClock = 0;
    }

    /**
     * Constructor for Statistics
     * @param queryStatistics
     * @param connectionsModuleStatistics
     * @param processModuleStatistics
     * @param queryProcessingStatistics
     * @param transactionsModuleStatistics
     * @param executorModuleStatistics
     */
    Statistics(QueryStatistics queryStatistics, ModuleStatistics connectionsModuleStatistics, ModuleStatistics processModuleStatistics, ModuleStatistics queryProcessingStatistics, ModuleStatistics transactionsModuleStatistics, ModuleStatistics executorModuleStatistics, double finalClock) {
        this.queryStatistics = queryStatistics;
        this.connectionsModuleStatistics = connectionsModuleStatistics;
        this.processModuleStatistics = processModuleStatistics;
        this.queryProcessingStatistics = queryProcessingStatistics;
        this.transactionsModuleStatistics = transactionsModuleStatistics;
        this.executorModuleStatistics = executorModuleStatistics;
        this.finalClock = finalClock;
    }

    /**
     *
     * @param queryStatistics
     * @param connectionsModuleStatistics
     * @param processModuleStatistics
     * @param queryProcessingStatistics
     * @param transactionsModuleStatistics
     * @param executorModuleStatistics
     */
    public void setStatistics(QueryStatistics queryStatistics, ModuleStatistics connectionsModuleStatistics, ModuleStatistics processModuleStatistics, ModuleStatistics queryProcessingStatistics, ModuleStatistics transactionsModuleStatistics, ModuleStatistics executorModuleStatistics, double finalClock) {
        this.queryStatistics = queryStatistics;
        this.connectionsModuleStatistics = connectionsModuleStatistics;
        this.processModuleStatistics = processModuleStatistics;
        this.queryProcessingStatistics = queryProcessingStatistics;
        this.transactionsModuleStatistics = transactionsModuleStatistics;
        this.executorModuleStatistics = executorModuleStatistics;
        this.finalClock = finalClock;
    }


    /**
     *
     * @return
     */
    public double getFinalClock(){
        return finalClock;
    }
    /**
     *
     * @return
     */
    public QueryStatistics getQueryStatistics() {
        return queryStatistics;
    }

    /**
     *
     * @return
     */
    public ModuleStatistics getConnectionsModuleStatistics() {
        return connectionsModuleStatistics;
    }

    /**
     *
     * @return
     */
    public ModuleStatistics getProcessModuleStatistics() {
        return processModuleStatistics;
    }

    /**
     *
     * @return
     */
    public ModuleStatistics getQueryProcessingStatistics() {
        return queryProcessingStatistics;
    }

    /**
     *
     * @return
     */
    public ModuleStatistics getTransactionsModuleStatistics() {
        return transactionsModuleStatistics;
    }

    /**
     *
     * @return
     */
    public ModuleStatistics getExecutorModuleStatistics() {
        return executorModuleStatistics;
    }
}