import java.util.Vector;

public class GeneralStatistics {

    private Statistics statistics;
    private Vector<Statistics> statisticsVector;
    private int totalCounter;

    /**
     * Constructor for GeneralStatistics
     * @param statisticsVector
     */
    GeneralStatistics(Vector<Statistics> statisticsVector){
        this.statistics = new Statistics();
        this.statisticsVector = statisticsVector;
        this.totalCounter = statisticsVector.size();
    }

    /**
     * Create a average statistics
     * @return average statistics
     */
    public Statistics getGeneralStatisitcs(){
        // Sets total query arrivals per sentence in the respective module
        statistics.getConnectionsModuleStatistics().setTotalSentenceQuery(totalCounter);
        statistics.getProcessModuleStatistics().setTotalSentenceQuery(totalCounter);
        statistics.getQueryProcessingStatistics().setTotalSentenceQuery(totalCounter);
        statistics.getTransactionsModuleStatistics().setTotalSentenceQuery(totalCounter);
        statistics.getExecutorModuleStatistics().setTotalSentenceQuery(totalCounter);
        // Sets total served clients per module
        statistics.getConnectionsModuleStatistics().setServedClients(totalCounter);
        statistics.getProcessModuleStatistics().setServedClients(totalCounter);
        statistics.getQueryProcessingStatistics().setServedClients(totalCounter);
        statistics.getTransactionsModuleStatistics().setServedClients(totalCounter);
        statistics.getExecutorModuleStatistics().setServedClients(totalCounter);
        for (int index=0; index<statisticsVector.size(); index++){
            // Sets total arrival rate per module
            statistics.getConnectionsModuleStatistics().setArrivedClients(statisticsVector.get(index).getConnectionsModuleStatistics().getArrivalRate(statisticsVector.get(index).getFinalClock()));
            statistics.getProcessModuleStatistics().setArrivedClients(statisticsVector.get(index).getProcessModuleStatistics().getArrivalRate(statisticsVector.get(index).getFinalClock()));
            statistics.getQueryProcessingStatistics().setArrivedClients(statisticsVector.get(index).getQueryProcessingStatistics().getArrivalRate(statisticsVector.get(index).getFinalClock()));
            statistics.getTransactionsModuleStatistics().setArrivedClients(statisticsVector.get(index).getTransactionsModuleStatistics().getArrivalRate(statisticsVector.get(index).getFinalClock()));
            statistics.getExecutorModuleStatistics().setArrivedClients(statisticsVector.get(index).getExecutorModuleStatistics().getArrivalRate(statisticsVector.get(index).getFinalClock()));
            // Sets total time in module
            statistics.getConnectionsModuleStatistics().setTotalTimeInModule(statisticsVector.get(index).getConnectionsModuleStatistics().getAverageTimeInModule());
            statistics.getProcessModuleStatistics().setTotalTimeInModule(statisticsVector.get(index).getProcessModuleStatistics().getAverageTimeInModule());
            statistics.getQueryProcessingStatistics().setTotalTimeInModule(statisticsVector.get(index).getQueryProcessingStatistics().getAverageTimeInModule());
            statistics.getTransactionsModuleStatistics().setTotalTimeInModule(statisticsVector.get(index).getTransactionsModuleStatistics().getAverageTimeInModule());
            statistics.getExecutorModuleStatistics().setTotalTimeInModule(statisticsVector.get(index).getExecutorModuleStatistics().getAverageTimeInModule());
            // Sets total queue time in module
            statistics.getConnectionsModuleStatistics().setTotalTimeInQueue(statisticsVector.get(index).getConnectionsModuleStatistics().getAverageQueueTime());
            statistics.getProcessModuleStatistics().setTotalTimeInQueue(statisticsVector.get(index).getProcessModuleStatistics().getAverageQueueTime());
            statistics.getQueryProcessingStatistics().setTotalTimeInQueue(statisticsVector.get(index).getQueryProcessingStatistics().getAverageQueueTime());
            statistics.getTransactionsModuleStatistics().setTotalTimeInQueue(statisticsVector.get(index).getTransactionsModuleStatistics().getAverageQueueTime());
            statistics.getExecutorModuleStatistics().setTotalTimeInQueue(statisticsVector.get(index).getExecutorModuleStatistics().getAverageQueueTime());
            //Sets total idle rate per module
            statistics.getConnectionsModuleStatistics().setModuleIdleTime(statisticsVector.get(index).getConnectionsModuleStatistics().getIdleTime(),totalCounter);
            statistics.getProcessModuleStatistics().setModuleIdleTime(statisticsVector.get(index).getProcessModuleStatistics().getIdleTime(),totalCounter);
            statistics.getQueryProcessingStatistics().setModuleIdleTime(statisticsVector.get(index).getQueryProcessingStatistics().getIdleTime(),totalCounter);
            statistics.getTransactionsModuleStatistics().setModuleIdleTime(statisticsVector.get(index).getTransactionsModuleStatistics().getIdleTime(),totalCounter);
            statistics.getExecutorModuleStatistics().setModuleIdleTime(statisticsVector.get(index).getExecutorModuleStatistics().getIdleTime(),totalCounter);
            // Sets DDL total time in module
            statistics.getConnectionsModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(1),1);
            statistics.getProcessModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getProcessModuleStatistics().getAverageTimeInModulePerSentence(1), 1);
            statistics.getQueryProcessingStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getQueryProcessingStatistics().getAverageTimeInModulePerSentence(1), 1);
            statistics.getTransactionsModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(1), 1);
            statistics.getExecutorModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getExecutorModuleStatistics().getAverageTimeInModulePerSentence(1), 1);
            // Sets UPDATE total time in module
            statistics.getConnectionsModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(2),2);
            statistics.getProcessModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getProcessModuleStatistics().getAverageTimeInModulePerSentence(2), 2);
            statistics.getQueryProcessingStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getQueryProcessingStatistics().getAverageTimeInModulePerSentence(2), 2);
            statistics.getTransactionsModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(2), 2);
            statistics.getExecutorModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getExecutorModuleStatistics().getAverageTimeInModulePerSentence(2), 2);
            // Sets JOIN total time in module
            statistics.getConnectionsModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(3),3);
            statistics.getProcessModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getProcessModuleStatistics().getAverageTimeInModulePerSentence(3), 3);
            statistics.getQueryProcessingStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getQueryProcessingStatistics().getAverageTimeInModulePerSentence(3), 3);
            statistics.getTransactionsModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(3), 3);
            statistics.getExecutorModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getExecutorModuleStatistics().getAverageTimeInModulePerSentence(3), 3);
            // Sets SELECT total time in module
            statistics.getConnectionsModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(4),4);
            statistics.getProcessModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getProcessModuleStatistics().getAverageTimeInModulePerSentence(4), 4);
            statistics.getQueryProcessingStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getQueryProcessingStatistics().getAverageTimeInModulePerSentence(4), 4);
            statistics.getTransactionsModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(4), 4);
            statistics.getExecutorModuleStatistics().setAverageTimeInModulePerSentence(statisticsVector.get(index).getExecutorModuleStatistics().getAverageTimeInModulePerSentence(4), 4);
            // Sets average discarded connections, total connections and connections life time
            statistics.getQueryStatistics().setDiscardedConnections(statisticsVector.get(index).getQueryStatistics().getDiscardedConnections(),totalCounter);
            statistics.getQueryStatistics().setTotalConnections(statisticsVector.get(index).getQueryStatistics().getTotalConnections(),totalCounter);
            statistics.getQueryStatistics().setAverageConnectionLifeTime(statisticsVector.get(index).getQueryStatistics().getTotalAverageConnectionLifeTime(),totalCounter);
            }
            return statistics;
    }
}