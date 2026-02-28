package com.cookiegramstudios.cookiegram.receipt;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_receipts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private int orderNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(columnDefinition = "TEXT")
    private String summaryText;

    private double totalPrice;

    private LocalDate deliveryDate;

    private LocalDateTime createdAt;

    private boolean emailSent;

    private LocalDateTime emailSentAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
