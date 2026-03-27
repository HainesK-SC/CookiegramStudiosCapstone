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

}
