package com.cookiegramstudios.cookiegram.order.dto;

public class OrderDTO {
    private Long customerId;
    private Long recipientId;
    private LocalDate deliveryDate;
    private double totalPrice;
    private String notes;

    public OrderDTO() {
    }
}
