package com.cookiegramstudios.cookiegram.order.dto;

public class OrderDTO {
    private Long customerId;
    private Long recipientId;
    private LocalDate deliveryDate;
    private double totalPrice;
    private String notes;

    public OrderDTO() {
    }

    public OrderDTO(Long customerId, Long recipientId, LocalDate deliveryDate, double totalPrice, String notes) {
        this.customerId = customerId;
        this.recipientId = recipientId;
        this.deliveryDate = deliveryDate;
        this.totalPrice = totalPrice;
        this.notes = notes;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
