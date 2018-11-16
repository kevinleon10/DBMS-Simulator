public class QueryStatistics {

    private double averageConnectionLifeTime; //Tiempo promedio que dura una consulta en el sistema.
    private int totalConnections; // Número total de conexiones que hubieron en el sistema.
    private int discardedConnections; //Consultas descartadas que no pudieron entrar al sistema.

    public QueryStatistics() {
        this.averageConnectionLifeTime = 0;
        this.totalConnections = 0;
        this.discardedConnections = 0;
    }

    public void increaseAverageConnectionLifeTime(double connectionTime){
        averageConnectionLifeTime += connectionTime;
    }

    public void increaseDiscardedConnections(){
        discardedConnections++;
    }

    public void increaseTotalConnections(){
        totalConnections++;
    }

    public double getTotalAverageConnectionLifeTime(){
        return averageConnectionLifeTime/totalConnections;
    }
    
    public int getTotalConnections() {
        return totalConnections;
    }

    public int getDiscardedConnections() {
        return discardedConnections;
    }

    public void setAverageConnectionLifeTime(double averageConnectionLifeTime, int counter) {
        this.averageConnectionLifeTime += (averageConnectionLifeTime/counter);
    }

    public void setDiscardedConnections(int discardedConnections, int counter) {
        this.discardedConnections += (discardedConnections/counter);
    }

    public void setTotalConnections(int totalConnections, int counter){
        this.totalConnections +=(totalConnections/counter);
    }
}