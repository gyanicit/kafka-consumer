package com.icit.dto;

import java.time.Instant;

public class OrderEvent {
    private String orderId;
    private int sequence;
    private String eventType;
    private Instant createdOn;

    public OrderEvent() {
    }

    public OrderEvent(String orderId, int sequence, String eventType, Instant createdOn) {
        this.orderId = orderId;
        this.sequence = sequence;
        this.eventType = eventType;
        this.createdOn = createdOn;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "orderId='" + orderId + '\'' +
                ", sequence=" + sequence +
                ", eventType='" + eventType + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
