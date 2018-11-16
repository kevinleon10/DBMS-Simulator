public enum QueryType {
    DDL(false, 1),
    UPDATE(false, 2),
    JOIN(true, 3),
    SELECT(true, 4);

    private boolean isReadOnly; // Indica si la consulta es Read Only
    private int priority; // Prioridad de cada consulta

    /**
     * Constructor for ConnectionsModule
     * @param isReadOnly if the query type is read only
     * @param priority the priority of each query type
     */
    QueryType(boolean isReadOnly, int priority){
        this.isReadOnly = isReadOnly;
        this.priority = priority;
    }

    /**
     * Returns true if query is read only, false otherwise
     * @return true if query is read only, false otherwise
     */
    public boolean isReadOnly() {
        return isReadOnly;
    }

    /**
     * Returns priority as an integer
     * @return query priority as an integer
     */
    public int getPriority() {
        return priority;
    }
}