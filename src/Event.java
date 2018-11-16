public class Event {

    private Query queryReference; // Query associated to the event
    private EventType eventType; // Event type
    private double eventTime; // Time for the event to happen

    /**
     * Empty constructor for Event
     */
    public Event(){

    }
    /**
     * Constructor for Event
     * @param eventType type of event
     * @param query query associated to the event
     * @param eventTime time for the event to happen
     */
    public Event(EventType eventType, Query query, double eventTime) {
        this.eventType = eventType;
        this.queryReference = query;
        this.eventTime = eventTime;
    }

    /**
     * Returns the event type
     * @return the event type
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Returns the query associated to the event
     * @return the query associated to the event
     */
    public Query getQueryReference() {
        return queryReference;
    }

    /**
     * Returns the time for the event to happen
     * @return the time for the event to happen
     */
    public double getEventTime() {
        return eventTime;
    }
}