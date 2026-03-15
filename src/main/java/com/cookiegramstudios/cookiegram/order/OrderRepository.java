package com.cookiegramstudios.cookiegram.order;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	
    List<Order> findByDeliveryDateOrderByCreatedAtAsc(LocalDate deliveryDate);
    

    List<Order> findByStatus(OrderStatus status);
    

    List<Order> findByDeliveryDateAndStatus(LocalDate deliveryDate, OrderStatus status);
    
   
    Order findByOrderNumber(int orderNumber);

}
