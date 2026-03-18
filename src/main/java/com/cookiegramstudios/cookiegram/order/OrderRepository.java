package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Order entity data access.
 * <p>
 * Provides database query methods for retrieving and managing orders.
 * Custom query methods follow Spring Data JPA naming conventions.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.1
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	
    List<Order> findByDeliveryDateOrderByCreatedAtAsc(LocalDate deliveryDate);
    

    List<Order> findByStatus(OrderStatus status);
    

    List<Order> findByDeliveryDateAndStatus(LocalDate deliveryDate, OrderStatus status);
    
   
    Order findByOrderNumber(int orderNumber);
    
    Optional<Order> findTopByOrderNumberDesc();

}
