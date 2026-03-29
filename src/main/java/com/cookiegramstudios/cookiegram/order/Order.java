package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cookiegramstudios.cookiegram.customer.Customer;
import com.cookiegramstudios.cookiegram.recipient.Recipient;
import com.cookiegramstudios.cookiegram.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;


/**
 * Represents a production order within the CookieGram system.
 * <p>
 * Orders are internal entities that track customer purchases, recipient
 * details, and order workflow statuses. This entity is used for managing order
 * processing, delivery scheduling, and administrative notes.
 * </p>
 * <p>
 * <b>Fields include:</b>
 * <ul>
 * <li><b>id</b> - Unique identifier for the order</li>
 * <li><b>orderNumber</b> - System-generated unique number for tracking</li>
 * <li><b>customerProfile</b> - The customer who placed the order</li>
 * <li><b>recipientUser</b> - Recipient of the order</li>
 * <li><b>status</b> - Current workflow status (PLACED, IN_PROGRESS, BAKED,
 * SHIPPED, DELIVERED)</li>
 * <li><b>deliveryDate</b> - Scheduled delivery date</li>
 * <li><b>totalPrice</b> - Total amount of the order</li>
 * <li><b>notes</b> - Optional administrative or special instructions</li>
 * <li><b>createdAt</b> - Timestamp when the order was created</li>
 * <li><b>updatedAt</b> - Timestamp when the order was last updated</li>
 * </ul>
 * </p>
 * <p>
 * <b>Future Considerations:</b>
 * <ul>
 * <li>Integrate with receipts and payment records</li>
 * <li>Support automated status updates for delivery tracking</li>
 * <li>Enhance auditing and reporting features</li>
 * </ul>
 * </p>
 * 
 * @author Matthew Samaha
 * @date 2026-02-28
 * @version 1.0
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int orderNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customerProfile;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Recipient recipientUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private LocalDate deliveryDate;

    private double totalPrice;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private boolean approved = false;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    private LocalDateTime approvedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedat;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedat = LocalDateTime.now();
    }

    public Order() {
    }

    public Order(int orderNumber, Customer customerProfile, Recipient recipientUser, OrderStatus status,
                 LocalDate deliveryDate, double totalPrice, String notes, boolean approved, User approvedBy,
                 LocalDateTime approvedAt, LocalDateTime createdAt, LocalDateTime updatedat) {
        this.orderNumber = orderNumber;
        this.customerProfile = customerProfile;
        this.recipientUser = recipientUser;
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.totalPrice = totalPrice;
        this.notes = notes;
        this.approved = approved;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
        this.createdAt = createdAt;
        this.updatedat = updatedat;
    }

    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }

    public void clearItems() {
        for (OrderItem item : this.items) {
            item.setOrder(null);
        }
        this.items.clear();
    }

    public Long getId() {
        return id;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Customer getCustomerProfile() {
        return customerProfile;
    }

    public void setCustomerProfile(Customer customerProfile) {
        this.customerProfile = customerProfile;
    }

    public Recipient getRecipientUser() {
        return recipientUser;
    }

    public void setRecipientUser(Recipient recipientUser) {
        this.recipientUser = recipientUser;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
    	this.createdAt = createdAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(LocalDateTime updatedat) {
        this.updatedat = updatedat;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.clearItems();
        if (items != null) {
            for (OrderItem item : items) {
                this.addItem(item);
            }
        }
    }

    public boolean isTerminal() {
        return this.status == OrderStatus.DELIVERED || this.status == OrderStatus.CANCELLED;
    }
    
    @Override
    public String toString() {
        return "Order [id=" + id + ", orderNumber=" + orderNumber + ", customerProfile=" + customerProfile
                + ", recipientUser=" + recipientUser + ", status=" + status + ", deliveryDate=" + deliveryDate
                + ", totalPrice=" + totalPrice + ", notes=" + notes + ", approved=" + approved + ", approvedBy=" + approvedBy
                + ", approvedAt=" + approvedAt + ", createdAt=" + createdAt + ", updatedat="
                + updatedat + ", itemsCount=" + (items != null ? items.size() : 0) + "]";
    }
}
